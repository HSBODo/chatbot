package site.pointman.chatbot.domain.member;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "tb_kakao_member")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class KakaoMember {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;
    @Id
    @Column(name = "kakao_userkey")
    private String kakaoUserkey ;
    @Column(name = "partner_id")
    private String partnerId ;
}
