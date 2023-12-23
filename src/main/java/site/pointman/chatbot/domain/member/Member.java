package site.pointman.chatbot.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.utill.CustomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
    @NotBlank
    @Column(name = "user_key")
    private String userKey;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @Embedded
    private Profile profile;

    @Convert(converter = MemberRoleEnumConverter.class)
    @NotNull
    private MemberRole role;

    @Builder
    public Member(String userKey, String name, String phoneNumber , Profile profile, MemberRole memberRole) {
        this.userKey = userKey;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.role = memberRole;
    }

    public String getFormatCreateDate(){
      return CustomStringUtils.dateFormat(getCreateDate(), "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
    }

    public void changePhoneNumber(String updatePhoneNumber){
        this.phoneNumber = updatePhoneNumber;
    }
    public void changeRole(MemberRole role){
        this.role = role;
    }
    public void changeUserKey(String userKey){
        this.userKey = userKey;
    }
    public void changeName(String name){
        this.name = name;
    }
}
