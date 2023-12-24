package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;

import java.util.Optional;

public interface PaymentRepository {
    PaymentInfo save(PaymentInfo order);
    Optional<PaymentInfo> findByPaymentStatus(Long orderId, PaymentStatus status);
    Optional<PaymentInfo> findByOrderId(Long orderId);
}
