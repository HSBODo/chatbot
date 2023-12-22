package site.pointman.chatbot.service;

public interface MemberService {
    Object join(String userKey, String name, String phoneNumber, boolean isChatBotRequest);
    Object getCustomerProfile(String userKey, boolean isChatBotRequest);
    Object updateCustomerPhoneNumber(String userKey, String updatePhoneNumber,boolean isChatBotRequest);
    Object withdrawalCustomer(String userKey, boolean isChatBotRequest);
    boolean isCustomer(String userKey);
}
