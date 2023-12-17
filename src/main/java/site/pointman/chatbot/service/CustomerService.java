package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ValidationResponse;

public interface CustomerService {
    ChatBotResponse join(String userKey, String name, String phoneNumber);
    ChatBotResponse getCustomerProfile(String userKey);
    ChatBotResponse updateCustomerPhoneNumber(String userKey, String updatePhoneNumber);
    ChatBotResponse deleteCustomer(String userKey);
    boolean isCustomer(ChatBotRequest chatBotRequest);
    boolean isCustomer(String userKey);
}
