package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.payment.PaymentInfo;

import java.util.Optional;

public interface PaymentRepository {
    PaymentInfo savePayReady(PaymentInfo order);
    Optional<PaymentInfo> findByPaymentReady(Long orderId);
    Optional<PaymentInfo> findByApproveOrder(Long orderId);
}
