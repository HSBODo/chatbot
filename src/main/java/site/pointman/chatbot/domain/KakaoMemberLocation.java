package site.pointman.chatbot.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_kakao_member_location")
@Getter @Setter
public class KakaoMemberLocation extends KakaoMember {
    @Column(name = "X")
    private String x ;
    @Column(name = "Y")
    private String y ;
}
