package site.pointman.chatbot.domain.order.service;

import site.pointman.chatbot.constant.order.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.response.Response;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Response purchaseConfirm(String orderId);
    Response salesConfirm(String orderId);
    Response updateTrackingNumber(String orderId, String trackingNumber);
    Response addOrder(Long orderId, String pgToken);
    Response cancelOrder(Long orderId);
    Response successOrder(Long orderId);
    List<Order> getOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
    Optional<Order> getOrder(Long orderId);
}
