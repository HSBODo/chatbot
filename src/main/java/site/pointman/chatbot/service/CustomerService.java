package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.response.Response;

public interface CustomerService {
    Response join(String userKey, String name, String phoneNumber, boolean isChatBotRequest);
    Response getCustomerProfile(String userKey, boolean isChatBotRequest);
    Response updateCustomerPhoneNumber(String userKey, String updatePhoneNumber,boolean isChatBotRequest);
    Response deleteCustomer(String userKey, boolean isChatBotRequest);
    boolean isCustomer(String userKey);
}
