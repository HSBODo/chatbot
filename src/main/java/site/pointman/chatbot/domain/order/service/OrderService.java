package site.pointman.chatbot.domain.order.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.log.response.Response;

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

    Order getSalesContractProduct(String userKey, Long orderId);
    Page<Order> getPurchaseProducts(String userKey, int pageNumber);
    Order getPurchaseProduct(String userKey, Long orderId);
}
