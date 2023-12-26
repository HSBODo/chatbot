package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface CustomerChatBotResponseService {
    ChatBotResponse joinChatBotResponse(String userKey, String name, String phoneNumber);
    ChatBotResponse getCustomerProfileChatBotResponse(String userKey);
    ChatBotResponse updateCustomerPhoneNumberBotResponse(String userKey, String updatePhoneNumber);
    ChatBotResponse withdrawalCustomerChatBotResponse(String userKey);
    ChatBotResponse getMyPageSuccessChatBotResponse();
    ChatBotResponse getSalesCategorySuccessChatBotResponse();
    ChatBotResponse updateCustomerProfileImageChatBotResponse(String userKey, String profileImageUrl);
}
