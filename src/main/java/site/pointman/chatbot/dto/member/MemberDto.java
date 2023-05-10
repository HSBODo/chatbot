package site.pointman.chatbot.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.Platform;
import site.pointman.chatbot.domain.member.RoleType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberDto {

    private String kakaoUserkey ;
    private String id;
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    private String password;
    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phone;
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email
    private String email;
    private String baseAddress;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Builder
    public MemberDto(String kakaoUserkey, String id, String name, String password, String phone, String email, String baseAddress, RoleType roleType, Platform platform) {
        this.kakaoUserkey = kakaoUserkey;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.baseAddress = baseAddress;
        this.roleType = roleType;
        this.platform = platform;
    }

    public Member toEntity(){
        return Member.builder()
                .kakaoUserkey(this.kakaoUserkey)
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .phone(this.phone)
                .email(this.email)
                .baseAddress(this.baseAddress)
                .roleType(this.roleType)
                .platform(this.platform)
                .build();
    }
}
