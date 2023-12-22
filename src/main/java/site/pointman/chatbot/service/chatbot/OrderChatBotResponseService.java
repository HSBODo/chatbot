package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.response.ChatBotResponse;

import java.util.List;

public interface OrderChatBotResponseService {
    ChatBotResponse getPurchaseProducts(List<Order> purchaseOrders);
    ChatBotResponse getPurchaseProductProfile(Order order);
    ChatBotResponse purchaseSuccessReconfirm(String orderId);
    ChatBotResponse purchaseSuccessConfirm(Order order);
    ChatBotResponse updateTrackingNumber();
}
