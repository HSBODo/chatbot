package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;

public interface OrderService {
    ChatBotResponse purchaseSuccessReconfirm(String orderId);
    ChatBotResponse purchaseSuccessConfirm(String orderId);
    ChatBotResponse saleSuccessReconfirm(String orderId);
    ChatBotResponse saleSuccessConfirm(String orderId);
    ChatBotResponse updateTrackingNumber(String orderId, String trackingNumber);
    ChatBotResponse getPurchaseProducts(String userKey);
    ChatBotResponse getPurchaseProductProfile(String userKey,String orderId);
    Long addOrder(Long orderId, String pgToken);
    Long cancelOrder(Long orderId);
    HttpResponse successOrder(Long orderId);
    Object getOrders();
    Object getOrders(OrderStatus status);
    Object getOrder(Long orderId);
}
