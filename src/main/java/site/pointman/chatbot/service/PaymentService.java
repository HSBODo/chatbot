package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentApproveResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    KakaoPaymentReadyResponse getKakaoPaymentReadyUrl(Long productId, String userKey) throws UnsupportedEncodingException;
    KakaoPaymentApproveResponse kakaoPaymentApprove(Long orderId, String pgToken) throws Exception;
    KakaoPaymentCancelResponse kakaoPaymentCancel(Long orderId) throws Exception;
}
