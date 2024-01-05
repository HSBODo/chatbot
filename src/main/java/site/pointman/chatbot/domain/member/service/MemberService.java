package site.pointman.chatbot.domain.member.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;

import java.util.Optional;

public interface MemberService {
    Response join(String userKey, String name, String phoneNumber);
    Page<MemberProfileDto> getMemberProfiles(int page);
    Optional<MemberProfileDto> getMemberProfileDto(String userKey);
    Optional<MemberProfileDto> getMemberProfileDtoByName(String name);
    Response updateMember(String name, MemberProfileDto memberProfileDto);
    Response updateMemberPhoneNumber(String userKey, String updatePhoneNumber);
    Response deleteMember(String userKey);
    Response updateMemberProfileImage(String userKey, String profileImageUrl);
    boolean isCustomer(String userKey);
    boolean isCustomerByName(String name);
    boolean isAdmin(String name, String userKey);
    boolean isDuplicationName(String name);
}
