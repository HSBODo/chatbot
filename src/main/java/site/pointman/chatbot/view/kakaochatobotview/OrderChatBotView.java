package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface OrderChatBotView {
    ChatBotResponse purchaseReconfirm(String orderId);
    ChatBotResponse purchaseConfirm(String orderId);
    ChatBotResponse salesReconfirm(String orderId);
    ChatBotResponse salesConfirm(String orderId);
    ChatBotResponse updateTrackingNumber(String orderId, String trackingNumber);
}
