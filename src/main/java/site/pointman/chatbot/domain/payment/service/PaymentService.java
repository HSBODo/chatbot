package site.pointman.chatbot.domain.payment.service;

import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentApproveResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.log.response.Response;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    KakaoPaymentReadyResponse kakaoPaymentReady(Long productId, String userKey) throws UnsupportedEncodingException;
    PaymentInfo kakaoPaymentApprove(String pgToken, PaymentInfo paymentReadyInfo);
    KakaoPaymentCancelResponse kakaoPaymentCancel(Long orderId);
    PaymentInfo getPaymentReady(Long orderId);
}
