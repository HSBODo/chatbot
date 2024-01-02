package site.pointman.chatbot.domain.member.dto;


import lombok.Getter;

@Getter
public class MemberJoinDto {
    private String userKey;
    private String name;
    private String phoneNumber;
}
