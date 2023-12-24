package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;

public interface MemberService {
    Object join(String userKey, String name, String phoneNumber, boolean isChatBotRequest);
    Object getCustomers();
    Object getCustomerProfile(String userKey, boolean isChatBotRequest);
    HttpResponse updateMember(String userKey, Member member);
    Object updateCustomerPhoneNumber(String userKey, String updatePhoneNumber,boolean isChatBotRequest);
    Object withdrawalCustomer(String userKey, boolean isChatBotRequest);
    ChatBotResponse updateCustomerProfileImage(String userKey, String profileImageUrl);
    boolean isCustomer(String userKey);
    boolean isAdmin(String name, String userKey);
}
