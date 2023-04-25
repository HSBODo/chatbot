package site.pointman.chatbot.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@Entity
@Table(name = "tb_member")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;

    @NotEmpty
    @Id
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    private String phone;

    private String email;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @NotEmpty
    private String partnerId;

    private String isUse;
    @Builder
    public Member(Long idx, String id, String name, String password, String phone, String email, RoleType roleType, String partnerId, String isUse) {
        this.idx = idx;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.roleType = roleType;
        this.partnerId = partnerId;
        this.isUse = isUse;
    }
    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .roleType(roleType)
                .id(id)
                .isUse(isUse)
                .password(password)
                .partnerId(partnerId)
                .phone(phone).build();
    }
}
