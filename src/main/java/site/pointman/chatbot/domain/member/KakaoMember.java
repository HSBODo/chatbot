package site.pointman.chatbot.domain.member;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_kakao_member")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class KakaoMember extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long idx ;
    @Id
    @Column(name = "kakao_userkey",nullable = false)
    private String kakaoUserkey ;
    @Column(name = "partner_id")
    private String partnerId ;
    @Builder
    public KakaoMember(Long idx, String kakaoUserkey, String partnerId) {
        this.idx = idx;
        this.kakaoUserkey = kakaoUserkey;
        this.partnerId = partnerId;
    }
}
