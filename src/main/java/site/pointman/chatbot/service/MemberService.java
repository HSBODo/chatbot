package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.member.MemberProfileDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Response join(String userKey, String name, String phoneNumber);
    List<Member> getMembers();
    Optional<Member> getMember(String userKey);
    Optional<MemberProfileDto> getMemberProfileDto(String userKey);
    Optional<Member> getMemberByName(String name);
    Response updateMember(String userKey, Member member);
    Response updateMemberPhoneNumber(String userKey, String updatePhoneNumber);
    Response deleteMember(String userKey);
    Response updateMemberProfileImage(String userKey, String profileImageUrl);
    boolean isCustomer(String userKey);
    boolean isCustomerByName(String name);
    boolean isAdmin(String name, String userKey);
    boolean isDuplicationName(String name);
}
