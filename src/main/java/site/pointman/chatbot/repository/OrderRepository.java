package site.pointman.chatbot.repository;

import site.pointman.chatbot.service.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findByApproveOrders(String kakaoUserkey);
    Order savePayReady(Order order);
    Optional<Order> findByReadyOrder(Long orderId);
    Optional<Order> findByApproveOrder(Long orderId);
    Optional<Order> findByOrder(String kakaoUserkey, Long orderId);
    Optional<Order> updatePayApprove(Order updateParams);
    Optional<Order> updatePayCancel(Order updateParams);
    List<Order> findBySalesRank();
}
