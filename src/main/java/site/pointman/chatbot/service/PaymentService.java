package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.payment.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentApproveResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.response.Response;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    KakaoPaymentReadyResponse kakaoPaymentReady(Long productId, String userKey) throws UnsupportedEncodingException;
    KakaoPaymentApproveResponse kakaoPaymentApprove(String pgToken, PaymentInfo paymentReadyInfo);
    KakaoPaymentCancelResponse kakaoPaymentCancel(PaymentInfo successPaymentInfo);
    Response getPaymentInfoByStatus(Long orderId, PaymentStatus paymentStatus);
}
