package site.pointman.chatbot.domain.member;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_kakao_member_location")
@Getter
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class KakaoMemberLocation extends BaseEntity {
    @Id
    @Column(name = "kakao_userkey")
    private String kakaoUserkey ;
    @Column(name = "X",precision = 10,scale = 7)
    private BigDecimal x ;
    @Column(name = "Y",precision = 10,scale = 7)
    private BigDecimal y ;
    @Builder
    public KakaoMemberLocation(String kakaoUserkey, BigDecimal x, BigDecimal y) {
        this.kakaoUserkey = kakaoUserkey;
        this.x = x;
        this.y = y;
    }
    public void changeX(BigDecimal x){
        this.x=x;
    }
    public void changeY(BigDecimal y){
        this.y=y;
    }
}
