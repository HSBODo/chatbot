package site.pointman.chatbot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class KakaoMemberLocationDto  {
    @NotBlank(message = "유저키는 필수입니다.")
    private String kakaoUserkey ;
    @NotBlank(message = "경도값은 필수입니다.")
    private BigDecimal x ;
    @NotBlank(message = "위도값은 필수입니다.")
    private BigDecimal y ;
    @Builder
    public KakaoMemberLocationDto(String kakaoUserkey, BigDecimal x, BigDecimal y) {
        this.kakaoUserkey = kakaoUserkey;
        this.x = x;
        this.y = y;
    }
    public KakaoMemberLocation toEntity(){
        return KakaoMemberLocation.builder()
                .kakaoUserkey(this.kakaoUserkey)
                .y(this.y)
                .x(this.x)
                .build();
    }
}
