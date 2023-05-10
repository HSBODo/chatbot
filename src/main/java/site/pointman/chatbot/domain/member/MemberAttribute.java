package site.pointman.chatbot.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.dto.member.MemberAttributeDto;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_member_attribute")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class MemberAttribute extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Id
    @Column(name = "kakao_userkey",nullable = false)
    private String kakaoUserkey ;
    @Column()
    private Long optionCode;
    @Column
    private int quantity;
    @Builder
    public MemberAttribute(Long id, String kakaoUserkey, Long optionCode, int quantity) {
        this.id = id;
        this.kakaoUserkey = kakaoUserkey;
        this.optionCode = optionCode;
        this.quantity = quantity;
    }


    public MemberAttributeDto toMemberAttributeDto(){
        return MemberAttributeDto.builder()
                .id(this.id)
                .kakaoUserkey(this.kakaoUserkey)
                .optionCode(this.optionCode)
                .quantity(this.quantity)
                .build();
    }

    public void changeOptionCode(Long optionCode) {
        this.optionCode = optionCode;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
