package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.custom.OrderCustomRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderCustomRepository {

}
