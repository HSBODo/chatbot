package site.pointman.chatbot.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.dto.member.MemberDto;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "tb_member")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Id
    private String kakaoUserkey ;
    private String id;

    private String name;

    private String password;

    private String phone;

    private String email;

    private String baseAddress;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Builder
    public Member(String kakaoUserkey, String id, String name, String password, String phone, String email, String baseAddress, RoleType roleType, Platform platform) {
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

    public MemberDto toMemberDto(){
        return MemberDto.builder()
                .kakaoUserkey(this.kakaoUserkey)
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .phone(this.phone)
                .email(this.email)
                .baseAddress(this.baseAddress)
                .build();
    }

    public void Withdrawal(){
        super.changeIsUse("N");
    }
}
