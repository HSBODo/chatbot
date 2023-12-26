package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.repository.custom.PaymentCustomRepository;

public interface PaymentRepository extends JpaRepository<PaymentInfo,Long>, PaymentCustomRepository {

}
