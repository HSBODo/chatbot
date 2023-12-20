package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.payment.PaymentInfo;

import java.util.Optional;

public interface PaymentRepository {
    PaymentInfo save(PaymentInfo order);
    Optional<PaymentInfo> findByPaymentReadyStatus(Long orderId);
    Optional<PaymentInfo> findByPaymentApproveStatus(Long orderId);
}
