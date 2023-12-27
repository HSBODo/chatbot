package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;

public interface OrderService {
    HttpResponse purchaseConfirm(String orderId);
    HttpResponse salesConfirm(String orderId);
    HttpResponse updateTrackingNumber(String orderId, String trackingNumber);
    HttpResponse addOrder(Long orderId, String pgToken);
    HttpResponse cancelOrder(Long orderId);
    HttpResponse successOrder(Long orderId);
    HttpResponse getOrders();
    HttpResponse getOrders(OrderStatus status);
    HttpResponse getOrder(Long orderId);
}
