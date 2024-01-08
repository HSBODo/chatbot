package site.pointman.chatbot.domain.payment.service;

import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.kakaopay.response.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.response.KakaoPaymentReadyResponse;

import java.io.UnsupportedEncodingException;

public interface PaymentService {

    /**
     * 카카오페이 결제 준비
     * 결제 준비 -> 결제 준비 정보 저장 -> 결제준비 완료
     */
    KakaoPaymentReadyResponse kakaoPaymentReady(Long productId, String userKey) throws UnsupportedEncodingException;


    /**
     * 카카오페이 결제 완료
     * 결제 시도 -> 결제 성공 -> 결제성공 정보 업데이트 -> 결제 완료
     */
    PaymentInfo kakaoPaymentApprove(String pgToken, PaymentInfo paymentReadyInfo);

    /**
     * 카카오페이 결제 취소
     * 결제완료 정보 선택 -> 결제 취소 성공 -> 결제 취소 정보 업데이트 -> 결제 취소 완료
     */
    KakaoPaymentCancelResponse kakaoPaymentCancel(Long orderId);
    PaymentInfo getPaymentReady(Long orderId);
}
