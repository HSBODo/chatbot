package site.pointman.chatbot.dto.member;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.member.MemberAttribute;

@Getter
@Setter
public class MemberAttributeDto {

    private Long id;
    private String kakaoUserkey ;
    private Long optionCode;
    private int quantity;
    @Builder
    public MemberAttributeDto(Long id, String kakaoUserkey, Long optionCode, int quantity) {
        this.id = id;
        this.kakaoUserkey = kakaoUserkey;
        this.optionCode = optionCode;
        this.quantity = quantity;
    }

    public MemberAttribute toEntity(){
        return MemberAttribute.builder()
                .optionCode(this.optionCode)
                .quantity(this.quantity)
                .kakaoUserkey(this.kakaoUserkey)
                .build();
    }
}
