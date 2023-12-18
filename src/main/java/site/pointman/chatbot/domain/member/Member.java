package site.pointman.chatbot.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "tb_member")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Member extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq ;

    @Id
    @Column(name = "user_key")
    private String userKey;
    private String name;
    private String phoneNumber;

    @Convert(converter = MemberRoleEnumConverter.class)
    private MemberRole role;

    @Builder
    public Member(String userKey, String name, String phoneNumber ,MemberRole memberRole) {
        this.userKey = userKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = memberRole;
    }

    public void changePhone(String updatePhoneNumber){
        phoneNumber = updatePhoneNumber;
    }
}
