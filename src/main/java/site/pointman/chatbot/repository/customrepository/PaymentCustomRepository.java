package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.constant.payment.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;

import java.util.Optional;

public interface PaymentCustomRepository {
    Optional<PaymentInfo> findByPaymentStatus(Long orderId, PaymentStatus status);
    Optional<PaymentInfo> findByOrderId(Long orderId);
}
