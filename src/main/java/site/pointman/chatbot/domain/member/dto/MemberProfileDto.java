package site.pointman.chatbot.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.pointman.chatbot.constant.member.MemberRole;

@Getter
public class MemberProfileDto {

    private String name;
    private String phoneNumber;
    private MemberRole role;
    private String joinDate;

    @Builder
    public MemberProfileDto(String name, String phoneNumber, MemberRole role, String joinDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.joinDate = joinDate;
    }
}
