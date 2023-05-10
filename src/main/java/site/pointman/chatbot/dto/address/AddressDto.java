package site.pointman.chatbot.dto.address;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import site.pointman.chatbot.domain.address.Address;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    private Long id ;
    @NotBlank
    private String kakaoUserkey ;
    @NotBlank(message = "우편번호를 입력해 주세요.")
    private String postCode ;
    @NotBlank(message = "받으실 분 성함을 입력해주세요.")
    private String name ;
    @NotBlank(message = "-없이 전화번호를 입력해 주세요.")
    private String phone ;
    @NotBlank(message = "도로명주소를 입력해 주세요.")
    private String roadAddress ;
    @NotBlank(message = "지번주소를 입력해 주세요.")
    private String jibunAddress ;
    @NotBlank(message = "상세주소를 입력해 주세요")
    private String detailAddress ;
    private String extraAddress ;

    @Builder
    public AddressDto(Long id, String kakaoUserkey, String postCode, String name, String phone, String roadAddress, String jibunAddress, String detailAddress, String extraAddress) {
        this.id = id;
        this.kakaoUserkey = kakaoUserkey;
        this.postCode = postCode;
        this.name = name;
        this.phone = phone;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }



    public Address toEntity(){
        return  Address.builder()
                .kakaoUserkey(this.kakaoUserkey)
                .name(this.name)
                .phone(this.phone)
                .postCode(this.postCode)
                .roadAddress(this.roadAddress)
                .jibunAddress(this.jibunAddress)
                .detailAddress(this.detailAddress)
                .extraAddress(this.extraAddress)
                .build();
    }
}


