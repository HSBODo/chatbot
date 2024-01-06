package site.pointman.chatbot.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchParamDto {
    private String userKey;
    private String name;

    @Builder
    public MemberSearchParamDto(String userKey, String name) {
        this.userKey = userKey;
        this.name = name;
    }

}
