package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.Response;

import java.util.List;

public interface MemberService {
    Response join(String userKey, String name, String phoneNumber);
    List<Member> getMembers();
    Member getMember(String userKey);
    Response updateMember(String userKey, Member member);
    Response updateMemberPhoneNumber(String userKey, String updatePhoneNumber);
    Response deleteMember(String userKey);
    Response updateMemberProfileImage(String userKey, String profileImageUrl);
    boolean isCustomer(String userKey);
    boolean isAdmin(String name, String userKey);
}
