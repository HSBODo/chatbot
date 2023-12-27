package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.response.ChatBotResponse;

import java.util.List;

public interface OrderChatBotResponseService {
    ChatBotResponse purchaseReconfirm(String orderId);
    ChatBotResponse purchaseConfirm(String orderId);
    ChatBotResponse salesReconfirm(String orderId);
    ChatBotResponse salesConfirm(String orderId);
    ChatBotResponse updateTrackingNumber(String orderId, String trackingNumber);
}
