package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface CustomerChatBotResponseService {
    ChatBotResponse joinSuccessChatBotResponse();
    ChatBotResponse getCustomerProfileSuccessChatBotResponse(String customerName, String customerPhoneNumber, String customerJoinDate);
    ChatBotResponse updateCustomerPhoneNumberSuccessChatBotResponse();
    ChatBotResponse deleteCustomerSuccessChatBotResponse();
}
