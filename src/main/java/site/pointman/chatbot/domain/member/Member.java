package site.pointman.chatbot.domain.member;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
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
    private String access;
    @NotEmpty
    private String partnerId;
    private String isUse;
}
