package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface MemberChatBotView {
    ChatBotResponse joinChatBotResponse(String userKey, String name, String phoneNumber);
    ChatBotResponse getCustomerProfileChatBotResponse(String userKey);
    ChatBotResponse updateCustomerPhoneNumberBotResponse(String userKey, String updatePhoneNumber);
    ChatBotResponse withdrawalCustomerChatBotResponse(String userKey);
    ChatBotResponse getMyPageSuccessChatBotResponse();
    ChatBotResponse getSalesCategorySuccessChatBotResponse();
    ChatBotResponse updateCustomerProfileImageChatBotResponse(String userKey, String profileImageUrl);
}
