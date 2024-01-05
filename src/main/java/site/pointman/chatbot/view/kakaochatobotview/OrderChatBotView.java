package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;

public interface OrderChatBotView {
    ChatBotResponse purchaseReconfirmPage(String orderId);
    ChatBotResponse purchaseConfirmResultPage(String orderId);
    ChatBotResponse salesReconfirmPage(String orderId);
    ChatBotResponse salesConfirmResultPage(String orderId);
    ChatBotResponse updateTrackingNumberResultPage(String orderId, String trackingNumber);
}
