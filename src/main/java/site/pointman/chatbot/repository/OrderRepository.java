package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Long save(Order order);
    Optional<Order> findByOrderId(Long orderId);
    List<Order> findByUserKey(String userKey);
    Optional<Order> findByProductId(Long productId, OrderStatus orderStatus);
}
