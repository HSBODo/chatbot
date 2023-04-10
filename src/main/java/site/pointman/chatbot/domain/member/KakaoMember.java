package site.pointman.chatbot.domain.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "tb_kakao_member")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class KakaoMember extends BaseEntity{
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;
    @Id
    @Column(name = "kakao_userkey")
    private String kakaoUserkey ;
    @Column(name = "partner_id")
    private String partnerId ;
    @Column(name = "is_use")
    private String isUse = "Y" ;
}
