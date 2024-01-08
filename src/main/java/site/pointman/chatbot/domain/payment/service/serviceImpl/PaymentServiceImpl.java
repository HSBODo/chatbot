package site.pointman.chatbot.domain.payment.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.constant.PayMethod;
import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.kakaopay.request.KakaoPaymentApproveRequest;
import site.pointman.chatbot.domain.payment.kakaopay.request.KakaoPaymentCancelRequest;
import site.pointman.chatbot.domain.payment.kakaopay.request.KakaoPaymentReadyRequest;
import site.pointman.chatbot.domain.payment.kakaopay.response.KakaoPaymentApproveResponse;
import site.pointman.chatbot.domain.payment.kakaopay.response.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.response.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.payment.service.PaymentService;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.exception.NotFoundPaymentInfo;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.utill.CustomNumberUtils;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
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
    private boolean isUse = true;

    private final String KAKAO_PAY_READY_API_URL ="https://kapi.kakao.com/v1/payment/ready";
    private final String KAKAO_PAY_APPROVE_API_URL ="https://kapi.kakao.com/v1/payment/approve";
    private final String KAKAO_PAY_CANCEL_API_URL ="https://kapi.kakao.com/v1/payment/cancel";

    private final RestTemplate restTemplate = new RestTemplate();

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public KakaoPaymentReadyResponse kakaoPaymentReady(Long productId, String userKey) {
        final int quantity = 1;
        final int taxFreeAmount = 0;
        final int vatAmount = 0;

        Optional<Member> mayBeMember = memberRepository.findByUserKey(userKey,isUse);
        if (mayBeMember.isEmpty()) throw new IllegalArgumentException("회원이 존재하지 않습니다.");

        Optional<Product> mayBeProduct = productRepository.findByProductId(productId, isUse);
        if (mayBeProduct.isEmpty()) throw new IllegalArgumentException("상품이 존재하지 않습니다");

        Product product = mayBeProduct.get();
        if (!product.isAvailablePurchase())throw new IllegalArgumentException("판매중인 상품이 아닙니다.");

        Long orderId = CustomNumberUtils.createNumberId();
        Member buyerMember = mayBeMember.get();

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


        //파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentReadyRequest.getConvertRequestEntity();

        //카카오페이 API 요청
        KakaoPaymentReadyResponse kakaoPaymentReadyResponse = requestKakaPayment(KAKAO_PAY_READY_API_URL, convertRequestEntity, KakaoPaymentReadyResponse.class);

        PaymentInfo paymentReady = PaymentInfo.createPaymentReady(
                orderId,
                CID,
                kakaoPaymentReadyResponse.getTid(),
                buyerMember,
                product,
                taxFreeAmount,
                vatAmount,
                quantity);


        paymentRepository.save(paymentReady);

        return kakaoPaymentReadyResponse;
    }

    @Override
    @Transactional
    public PaymentInfo kakaoPaymentApprove(String pgToken, PaymentInfo paymentReadyInfo) {
        Product product = paymentReadyInfo.getProduct();
        Member buyerMember = paymentReadyInfo.getBuyerMember();

        KakaoPaymentApproveRequest kakaoPaymentApproveRequest = KakaoPaymentApproveRequest.builder()
                .cid(paymentReadyInfo.getCid())
                .tid(paymentReadyInfo.getTid())
                .partnerOrderId(String.valueOf(paymentReadyInfo.getOrderId()))
                .partnerUserId(buyerMember.getUserKey())
                .pgToken(pgToken)
                .totalAmount(product.getPrice().intValue())
                .build();

        // 파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentApproveRequest.getConvertRequestEntity();

        //카카오페이 API 요청
        KakaoPaymentApproveResponse kakaoPaymentApproveResponse = requestKakaPayment(KAKAO_PAY_APPROVE_API_URL, convertRequestEntity, KakaoPaymentApproveResponse.class);

        PayMethod payMethod = PayMethod.getPayMethod(kakaoPaymentApproveResponse.getPaymentMethodType());

        //결제정보 상태변경
        paymentReadyInfo.successPayment(
                kakaoPaymentApproveResponse.getAid(),
                kakaoPaymentApproveResponse.getApprovedAt(),
                payMethod,
                kakaoPaymentApproveResponse.getCardInfo(),
                kakaoPaymentApproveResponse.getAmount()
        );

        return paymentReadyInfo;
    }

    @Override
    @Transactional
    public KakaoPaymentCancelResponse kakaoPaymentCancel(Long orderId){
        Optional<PaymentInfo> mayBeSuccessPaymentInfo = paymentRepository.findByPaymentStatus(orderId, PaymentStatus.결제완료);
        if (mayBeSuccessPaymentInfo.isEmpty()) throw new NotFoundPaymentInfo("결제완료된 결제정보가 존재하지 않습니다.");
        PaymentInfo successPaymentInfo = mayBeSuccessPaymentInfo.get();

        KakaoPaymentCancelRequest kakaoPaymentCancelRequest = successPaymentInfo.getKakaoPaymentCancelRequest();

        //파라미터
        MultiValueMap<String, String> convertRequestEntity = kakaoPaymentCancelRequest.getConvertRequestEntity();

        //카카오페이 API 요청
        KakaoPaymentCancelResponse kakaoPaymentCancelResponse = requestKakaPayment(KAKAO_PAY_CANCEL_API_URL, convertRequestEntity, KakaoPaymentCancelResponse.class);

        //결제취소
        successPaymentInfo.cancelPayment(kakaoPaymentCancelResponse.getCanceledAt());

        return kakaoPaymentCancelResponse;
    }

    @Override
    public PaymentInfo getPaymentReady(Long orderId) {
        PaymentStatus paymentStatus = PaymentStatus.결제준비;
        PaymentInfo paymentReady = paymentRepository.findByPaymentStatus(orderId, paymentStatus)
                .orElseThrow(() -> new NotFoundPaymentInfo("결제 준비상태의 결제정보가 존재하지 않습니다."));

        return paymentReady;
    }

    private <T> T requestKakaPayment(String requestUrl, MultiValueMap<String, String> convertRequestEntity, Class<T> responseType){
        //헤더
        HttpHeaders kakaoPayRequestHeaders = getKakaoPayRequestHeaders();

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(convertRequestEntity,kakaoPayRequestHeaders);

        // 외부에 보낼 url
        return restTemplate.postForObject(
                requestUrl,
                requestEntity,
                responseType);
    }

    private HttpHeaders getKakaoPayRequestHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }
}
