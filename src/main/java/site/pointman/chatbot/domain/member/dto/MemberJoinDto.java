package site.pointman.chatbot.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinDto {
    private String userKey;
    private String name;
    private String phoneNumber;

    @Builder
    public MemberJoinDto(String userKey, String name, String phoneNumber) {
        this.userKey = userKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
