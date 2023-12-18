package site.pointman.chatbot.domain.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
    private String phone;

    @Builder
    public Member(String userKey, String name, String phone) {
        this.userKey = userKey;
        this.name = name;
        this.phone = phone;
    }

    public void changePhone(String updatePhone){
        phone = updatePhone;
    }
}
