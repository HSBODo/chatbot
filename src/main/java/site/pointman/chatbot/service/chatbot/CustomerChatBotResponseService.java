package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface CustomerChatBotResponseService {
    ChatBotResponse joinSuccessChatBotResponse();
    ChatBotResponse getCustomerProfileSuccessChatBotResponse(Member member);
    ChatBotResponse updateCustomerPhoneNumberSuccessChatBotResponse();
    ChatBotResponse deleteCustomerSuccessChatBotResponse();
    ChatBotResponse getMyPageSuccessChatBotResponse();
    ChatBotResponse getSalesCategorySuccessChatBotResponse();
}
