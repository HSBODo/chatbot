package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface CustomerChatBotResponseService {
    ChatBotResponse joinSuccessChatBotResponse();
    ChatBotResponse getCustomerProfileSuccessChatBotResponse(String customerRank, String customerName, String customerPhoneNumber, String customerJoinDate);
    ChatBotResponse updateCustomerPhoneNumberSuccessChatBotResponse();
    ChatBotResponse deleteCustomerSuccessChatBotResponse();
    ChatBotResponse getMyPageSuccessChatBotResponse();
    ChatBotResponse getSalesHistorySuccessChatBotResponse();
}
