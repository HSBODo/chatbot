package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Long save(Order order);
    Optional<Order> findByOrderId(Long orderId);
    Optional<Order> findByOrderId(Long orderId,OrderStatus status);
    List<Order> findByBuyerUserKey(String buyerUserKey);
    Optional<Order> findByProductId(Long productId, OrderStatus orderStatus);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByAll();
}
