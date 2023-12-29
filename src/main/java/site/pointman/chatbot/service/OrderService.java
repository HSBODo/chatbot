package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.response.Response;

public interface OrderService {
    Response purchaseConfirm(String orderId);
    Response salesConfirm(String orderId);
    Response updateTrackingNumber(String orderId, String trackingNumber);
    Response addOrder(Long orderId, String pgToken);
    Response cancelOrder(Long orderId);
    Response successOrder(Long orderId);
    Response getOrders();
    Response getOrders(OrderStatus status);
    Response getOrder(Long orderId);
}
