package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;

public interface MemberService {
    Object join(String userKey, String name, String phoneNumber, boolean isChatBotRequest);

    Object getCustomers(boolean isChatBotRequest);
    Object getCustomerProfile(String userKey, boolean isChatBotRequest);
    Object updateMember(String userKey, Member member, boolean isChatBotRequest);
    Object updateCustomerPhoneNumber(String userKey, String updatePhoneNumber,boolean isChatBotRequest);
    Object withdrawalCustomer(String userKey, boolean isChatBotRequest);
    boolean isCustomer(String userKey);
}
