package site.pointman.chatbot.domain.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_kakao_member_location")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class KakaoMemberLocation extends BaseEntity {
    @Id
    @Column(name = "kakao_userkey")
    private String kakaoUserkey ;
    @Column(name = "X",precision = 10,scale = 7)
    private BigDecimal x ;
    @Column(name = "Y",precision = 10,scale = 7)
    private BigDecimal y ;
}
