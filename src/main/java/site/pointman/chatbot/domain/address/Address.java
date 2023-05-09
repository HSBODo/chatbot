package site.pointman.chatbot.domain.address;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.dto.AddressDto;
import site.pointman.chatbot.dto.KakaoMemberDto;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_member_address")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id ;
    @Column(name = "kakao_userkey",nullable = false)
    private String kakaoUserkey ;
    @Column(nullable = false)
    private String postCode ;
    @Column(nullable = false)
    private String name ;
    @Column(nullable = false)
    private String phone ;
    @Column(nullable = false)
    private String roadAddress ;
    @Column(nullable = false)
    private String jibunAddress ;
    @Column(nullable = false)
    private String detailAddress ;
    @Column(nullable = false)
    private String extraAddress ;
    @Builder
    public Address(Long id, String kakaoUserkey, String postCode, String name, String phone, String roadAddress, String jibunAddress, String detailAddress, String extraAddress) {
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

    public AddressDto toAddressDto(){
        return AddressDto.builder()
                .id(this.id)
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

    public void changePostCode(String postCode) {
        this.postCode = postCode;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void changeJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public void changeDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void changeExtraAddress(String extraAddress) {
        this.extraAddress = extraAddress;
    }
}
