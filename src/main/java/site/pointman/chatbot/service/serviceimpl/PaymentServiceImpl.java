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
    public KakaoPaymentReadyResponse getKakaoPaymentReadyUrl(Long productId, String userKey) throws UnsupportedEncodingException {
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


        KakaoPaymentReadyRequest kakaoPaymentReadyRequest = KakaoPaymentReadyRequest.builder()
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
        HttpHeaders kakaoPayRequestHeaders = getKakaoPayRequestHeaders();

        //파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentReadyRequest.getConvertRequestEntity();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,kakaoPayRequestHeaders);

        // 외부에 보낼 url
        KakaoPaymentReadyResponse kakaoPaymentReadyResponse = restTemplate.postForObject(
                KAKAO_PAY_READY_API_URL,
                requestEntity,
                KakaoPaymentReadyResponse.class);

        PaymentInfo paymentReady = PaymentInfo.builder()
                .orderId(orderId)
                .cid(CID)
                .tid(kakaoPaymentReadyResponse.getTid())
                .buyerMember(buyerMember)
                .product(product)
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .quantity(quantity)
                .status(PaymentStatus.결제준비)
                .build();

        paymentRepository.save(paymentReady);

        return kakaoPaymentReadyResponse;
    }

    @Override
    @Transactional
    public KakaoPaymentApproveResponse kakaoPaymentApprove(Long orderId, String pgToken) throws Exception {
        Optional<PaymentInfo> maybePaymentInfo = paymentRepository.findByPaymentReadyStatus(orderId);
        if(maybePaymentInfo.isEmpty()) throw new IllegalArgumentException("결제 준비 주문이 존재하지 않습니다.");

        PaymentInfo paymentReady = maybePaymentInfo.get();

        Product product = paymentReady.getProduct();
        Member buyerMember = paymentReady.getBuyerMember();

        KakaoPaymentApproveRequest kakaoPaymentApproveRequest = KakaoPaymentApproveRequest.builder()
                .cid(paymentReady.getCid())
                .tid(paymentReady.getTid())
                .partnerOrderId(String.valueOf(paymentReady.getOrderId()))
                .partnerUserId(buyerMember.getUserKey())
                .pgToken(pgToken)
                .totalAmount(product.getPrice().intValue())
                .build();

        //헤더
        HttpHeaders kakaoPayRequestHeaders = getKakaoPayRequestHeaders();

        // 파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentApproveRequest.getConvertRequestEntity();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,kakaoPayRequestHeaders);

        // 외부에 보낼 url
        KakaoPaymentApproveResponse kakaoPaymentApproveResponse = restTemplate.postForObject(
                KAKAO_PAY_APPROVE_API_URL,
                requestEntity,
                KakaoPaymentApproveResponse.class);

        PayMethod payMethod = PayMethod.getPayMethod(kakaoPaymentApproveResponse.getPaymentMethodType());

        paymentReady.changeStatus(PaymentStatus.결제승인);
        paymentReady.changeAid(kakaoPaymentApproveResponse.getAid());
        paymentReady.changeApprovedAt(kakaoPaymentApproveResponse.getApprovedAt());
        paymentReady.changePayMethod(payMethod);
        paymentReady.changeCardInfo(kakaoPaymentApproveResponse.getCardInfo());
        paymentReady.changeAmountInfo(kakaoPaymentApproveResponse.getAmount());

        orderService.addOrder(paymentReady);

        return kakaoPaymentApproveResponse;
    }

    @Override
    @Transactional
    public KakaoPaymentCancelResponse kakaoPaymentCancel(Long orderId) throws Exception {
        Optional<PaymentInfo> maybeOrder = paymentRepository.findByPaymentApproveStatus(orderId);

        if(maybeOrder.isEmpty()) throw new IllegalArgumentException("결제승인 주문이 존재하지 않습니다.");
        PaymentInfo approveOrder = maybeOrder.get();

        KakaoPaymentCancelRequest kakaoPaymentCancelRequest = new KakaoPaymentCancelRequest(
                approveOrder.getCid(),
                approveOrder.getTid(),
                approveOrder.getProduct().getPrice().intValue(),
                approveOrder.getTaxFreeAmount(),
                approveOrder.getVatAmount(),
                approveOrder.getProduct().getPrice().intValue()
        );

        //헤더
        HttpHeaders kakaoPayRequestHeaders = getKakaoPayRequestHeaders();

        //파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentCancelRequest.getConvertRequestEntity();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,kakaoPayRequestHeaders);

        // 외부에 보낼 url
        KakaoPaymentCancelResponse kakaoPaymentCancelResponse = restTemplate.postForObject(
                KAKAO_PAY_CANCEL_API_URL,
                requestEntity,
                KakaoPaymentCancelResponse.class);

        approveOrder.changeStatus(PaymentStatus.결제취소);

        return kakaoPaymentCancelResponse;
    }

    private HttpHeaders getKakaoPayRequestHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }
}
