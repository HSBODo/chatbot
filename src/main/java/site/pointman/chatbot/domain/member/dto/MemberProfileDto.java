package site.pointman.chatbot.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import site.pointman.chatbot.constant.member.MemberRole;

@Getter
@AllArgsConstructor
public class MemberProfileDto {
    private String nickname;
    private String phoneNumber;
    private MemberRole role;
    private String joinDate;
}
