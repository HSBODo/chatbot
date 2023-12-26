package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.HttpResponse;

import java.util.List;

public interface MemberService {
    HttpResponse join(String userKey, String name, String phoneNumber);
    HttpResponse getMembers();
    HttpResponse getMember(String userKey);
    HttpResponse updateMember(String userKey, Member member);
    HttpResponse updateMemberPhoneNumber(String userKey, String updatePhoneNumber);
    HttpResponse deleteMember(String userKey);
    HttpResponse updateMemberProfileImage(String userKey, String profileImageUrl);
    boolean isCustomer(String userKey);
    boolean isAdmin(String name, String userKey);
}
