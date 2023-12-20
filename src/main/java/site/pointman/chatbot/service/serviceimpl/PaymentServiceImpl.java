package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.constant.PaymentStatus;
import site.pointman.chatbot.domain.order.PayMethod;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.kakaopay.*;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;
import site.pointman.chatbot.utill.NumberUtils;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${kakao.channel.url}")
    private String KAKAO_CHANNEL_URL;

    @Value("${kakao.admin.key}")
    private String SERVICE_APP_ADMIN_KEY;

    @Value("${kakao.pay.cid}")
    private String CID;

    @Value("${kakao.pay.approval.host.url}")
    private String KAKAO_APPROVAL_HOST_URL;

    @Value("${kakao.pay.cancel.host.url}")
    private String KAKAO_CANCEL_HOST_URL;

    @Value("${kakao.pay.fail.host.url}")
    private String KAKAO_FAIL_HOST_URL;

    private final String KAKAO_PAY_READY_API_URL ="https://kapi.kakao.com/v1/payment/ready";
    private final String KAKAO_PAY_APPROVE_API_URL ="https://kapi.kakao.com/v1/payment/approve";
    private final String KAKAO_PAY_CANCEL_API_URL ="https://kapi.kakao.com/v1/payment/cancel";

    RestTemplate restTemplate = new RestTemplate();

    MemberRepository memberRepository;
    ProductRepository productRepository;
    PaymentRepository paymentRepository;
    OrderService orderService;

    public PaymentServiceImpl(MemberRepository memberRepository, ProductRepository productRepository, PaymentRepository paymentRepository, OrderService orderService) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Override
    public String getKakaoPaymentReadyUrl(Long productId, String userKey) throws UnsupportedEncodingException {
        try {
            final int quantity = 1;
            final int taxFreeAmount = 0;
            final int vatAmount = 0;

            Optional<Member> mayBeMember = memberRepository.findByUserKey(userKey);
            if (mayBeMember.isEmpty()) throw new IllegalArgumentException("회원이 존재하지 않습니다.");

            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if (mayBeProduct.isEmpty()) throw new IllegalArgumentException("상품이 존재하지 않습니다");

            Long orderId = NumberUtils.createNumberId();
            Member buyerMember = mayBeMember.get();
            Product product = mayBeProduct.get();


            PaymentReadyRequest paymentReadyRequest = PaymentReadyRequest.builder()
                    .cid(CID)
                    .partnerOrderId(String.valueOf(orderId))
                    .partnerUserId(buyerMember.getUserKey())
                    .itemName(product.getName())
                    .quantity(quantity)
                    .totalAmount(product.getPrice().intValue())
                    .vatAmount(vatAmount)
                    .taxFreeAmount(taxFreeAmount)
                    .approvalUrl(KAKAO_APPROVAL_HOST_URL+"/"+orderId)
                    .cancelUrl(KAKAO_CANCEL_HOST_URL+"/"+orderId)
                    .failUrl(KAKAO_FAIL_HOST_URL+"/"+orderId)
                    .build();

            //헤더
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
            headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> convertRequestEntity = paymentReadyRequest.getConvertRequestEntity();

            // 파라미터, 헤더
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,headers);

            // 외부에 보낼 url
            PaymentReadyResponse paymentReadyResponse = restTemplate.postForObject(
                    KAKAO_PAY_READY_API_URL,
                    requestEntity,
                    PaymentReadyResponse.class);

            PaymentInfo paymentReady = PaymentInfo.builder()
                    .orderId(orderId)
                    .cid(CID)
                    .tid(paymentReadyResponse.getTid())
                    .buyerMember(buyerMember)
                    .product(product)
                    .taxFreeAmount(taxFreeAmount)
                    .vatAmount(vatAmount)
                    .quantity(quantity)
                    .status(PaymentStatus.결제준비)
                    .build();

            paymentRepository.savePayReady(paymentReady);

            return paymentReadyResponse.getNext_redirect_app_url();
        }catch (Exception e) {
            return KAKAO_CHANNEL_URL+"/"+URLEncoder.encode("결제실패", "UTF-8");
        }
    }

    @Override
    @Transactional
    public String kakaoPaymentApprove(Long orderId, String pgToken) throws UnsupportedEncodingException {
        try {
            String resultRedirect;
            Optional<PaymentInfo> maybePaymentInfo = paymentRepository.findByPaymentReady(orderId);
            if(maybePaymentInfo.isEmpty()) throw new IllegalArgumentException("결제 준비 주문이 존재하지 않습니다.");

            PaymentInfo paymentReady = maybePaymentInfo.get();

            Product product = paymentReady.getProduct();
            Member buyerMember = paymentReady.getBuyerMember();

            //헤더
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
            headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            PaymentApproveRequest paymentApproveRequest = PaymentApproveRequest.builder()
                    .cid(paymentReady.getCid())
                    .tid(paymentReady.getTid())
                    .partnerOrderId(String.valueOf(paymentReady.getOrderId()))
                    .partnerUserId(buyerMember.getUserKey())
                    .pgToken(pgToken)
                    .totalAmount(product.getPrice().intValue())
                    .build();

            MultiValueMap<String, String> convertRequestEntity = paymentApproveRequest.getConvertRequestEntity();

            // 파라미터, 헤더
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,headers);

            // 외부에 보낼 url
            PaymentApproveResponse paymentApproveResponse = restTemplate.postForObject(
                    KAKAO_PAY_APPROVE_API_URL,
                    requestEntity,
                    PaymentApproveResponse.class);

            PayMethod payMethod = PayMethod.getPayMethod(paymentApproveResponse.getPaymentMethodType());

            paymentReady.changeStatus(PaymentStatus.결제승인);
            paymentReady.changeAid(paymentApproveResponse.getAid());
            paymentReady.changeApprovedAt(paymentApproveResponse.getApprovedAt());
            paymentReady.changePayMethod(payMethod);
            paymentReady.changeCardInfo(paymentApproveResponse.getCardInfo());
            paymentReady.changeAmountInfo(paymentApproveResponse.getAmount());

            orderService.addOrder(paymentReady);

            resultRedirect = URLEncoder.encode("결제성공", "UTF-8");
            return KAKAO_CHANNEL_URL+"/"+resultRedirect;
        }catch (Exception e) {
            log.info("e={}",e);
            String resultRedirect = URLEncoder.encode("결제실패", "UTF-8");
            return KAKAO_CHANNEL_URL+"/"+resultRedirect;
        }
    }

    @Override
    @Transactional
    public String kakaoPaymentCancel(Long orderId) throws UnsupportedEncodingException {
        try {

            Optional<PaymentInfo> maybeOrder = paymentRepository.findByApproveOrder(orderId);

            if(maybeOrder.isEmpty()) throw new IllegalArgumentException("결제승인 주문이 존재하지 않습니다.");
            PaymentInfo approveOrder = maybeOrder.get();

            PaymentCancelRequest paymentCancelRequest = new PaymentCancelRequest(
                    approveOrder.getCid(),
                    approveOrder.getTid(),
                    approveOrder.getProduct().getPrice().intValue(),
                    approveOrder.getTaxFreeAmount(),
                    approveOrder.getVatAmount(),
                    approveOrder.getProduct().getPrice().intValue()
            );

            //헤더
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
            headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // 파라미터, 헤더
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paymentCancelRequest.getConvertRequestEntity(),headers);

            // 외부에 보낼 url
            PaymentCancelResponse paymentCancelResponse = restTemplate.postForObject(
                    KAKAO_PAY_CANCEL_API_URL,
                    requestEntity,
                    PaymentCancelResponse.class);

            approveOrder.changeStatus(PaymentStatus.결제취소);

            String success = URLEncoder.encode("결제취소 성공", "UTF-8");
            return KAKAO_CHANNEL_URL+"/"+success;
        }catch (Exception e) {
            log.info("e={}",e);
            String fail = URLEncoder.encode("결제실패", "UTF-8");
            return KAKAO_CHANNEL_URL+"/"+fail;
        }
    }
}
