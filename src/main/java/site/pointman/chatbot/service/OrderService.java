package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;

public interface OrderService {
    Long addOrder(Long orderId, String pgToken);
    Long cancelOrder(Long orderId);
    HttpResponse successOrder(Long orderId);
    ChatBotResponse purchaseSuccessReconfirm(String orderId);
    ChatBotResponse purchaseSuccessConfirm(String orderId);
    ChatBotResponse saleSuccessReconfirm(String orderId);
    ChatBotResponse saleSuccessConfirm(String orderId);
    Object updateTrackingNumber(String orderId, String trackingNumber);
    Object getPurchaseProducts(String userKey);
    Object getPurchaseProductProfile(String userKey,String orderId);
}
