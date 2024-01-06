package site.pointman.chatbot.domain.member.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;

public interface MemberService {
    void join(String userKey, String name, String phoneNumber);
    Page<MemberProfileDto> getMemberProfiles(int page);
    MemberProfileDto getMemberProfileDto(String userKey);
    MemberProfileDto getMemberProfileDtoByName(String name);
    void updateMemberProfile(String name, MemberProfileDto memberProfileDto);
    void updateMemberPhoneNumber(String userKey, String updatePhoneNumber);
    void updateMemberProfileImage(String userKey, String profileImageUrl);
    void deleteMember(String userKey);
    boolean isCustomer(String userKey);
    boolean isCustomerByName(String name);
    boolean isAdmin(String userKey, String name);
    boolean isDuplicationName(String name);
}
