package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.customrepository.OrderCustomRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderCustomRepository {

}
