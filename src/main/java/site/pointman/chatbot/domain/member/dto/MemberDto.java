package site.pointman.chatbot.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import site.pointman.chatbot.constant.member.MemberRole;
import site.pointman.chatbot.domain.member.Member;

@Getter
public class MemberDto {
    private String userKey;
    private String name;
    private String phoneNumber;
    private MemberRole role;


    @Builder
    public MemberDto(String userKey, String name, String phoneNumber, MemberRole memberRole) {
        this.userKey = userKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = memberRole;
    }

    public Member toEntity(){
        return Member.builder()
                .userKey(userKey)
                .name(name)
                .phoneNumber(phoneNumber)
                .memberRole(role)
                .build();
    }
}
