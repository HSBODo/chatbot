package site.pointman.chatbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.member.KakaoMember;

import javax.validation.constraints.NotBlank;

/**
 * @NotNull : Null 값 체크
 * @NotEmpty : Null, "" 체크
 * @NotBlank : Null, "", 공백을 포함한 빈값 체크
 */

@Getter
@Setter
@NoArgsConstructor
public class KakaoMemberDto {
    private Long id ;
    @NotBlank(message = "유저키는 필수입니다.")
    private String kakaoUserkey ;
    private String partnerId ;
    @Builder
    public KakaoMemberDto(Long id, String kakaoUserkey, String partnerId) {
        this.id = id;
        this.kakaoUserkey = kakaoUserkey;
        this.partnerId = partnerId;
    }
    public KakaoMember toEntity(){
        return KakaoMember.builder()
                .kakaoUserkey(this.kakaoUserkey)
                .partnerId(this.partnerId)
                .build();
    }
}
