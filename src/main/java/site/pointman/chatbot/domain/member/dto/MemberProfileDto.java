package site.pointman.chatbot.domain.member.dto;


import lombok.Builder;
import lombok.Getter;
import site.pointman.chatbot.domain.member.constant.MemberRole;

import java.time.LocalDateTime;

@Getter
public class MemberProfileDto {

    private String name;
    private String phoneNumber;
    private MemberRole role;
    private LocalDateTime joinDate;

    @Builder
    public MemberProfileDto(String name, String phoneNumber, MemberRole role, LocalDateTime joinDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.joinDate = joinDate;
    }
}
