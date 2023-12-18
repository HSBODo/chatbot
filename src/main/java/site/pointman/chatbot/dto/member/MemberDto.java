package site.pointman.chatbot.dto.member;

import lombok.Builder;
import site.pointman.chatbot.domain.member.Member;

public class MemberDto {
    private String userKey;
    private String name;
    private String phoneNumber;


    @Builder
    public MemberDto(String userKey, String name, String phoneNumber) {
        this.userKey = userKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Member toEntity(){
        return Member.builder()
                .userKey(userKey)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }
}
