package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ValidationResponse;

public interface CustomerService {
    ChatBotResponse join(ChatBotRequest chatBotRequest);
    ChatBotResponse getCustomerInfo(ChatBotRequest chatBotRequest);
    ChatBotResponse updateCustomerPhoneNumber(ChatBotRequest chatBotRequest);
    ChatBotResponse deleteCustomer(ChatBotRequest chatBotRequest);
    boolean isCustomer(ChatBotRequest chatBotRequest);

}
