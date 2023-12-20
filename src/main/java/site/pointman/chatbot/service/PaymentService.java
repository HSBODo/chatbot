package site.pointman.chatbot.service;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    String getKakaoPaymentReadyUrl(Long productId, String userKey) throws UnsupportedEncodingException;
    String kakaoPaymentApprove(Long orderId, String pgToken) throws UnsupportedEncodingException;
    String kakaoPaymentCancel(Long orderId) throws UnsupportedEncodingException;
}
