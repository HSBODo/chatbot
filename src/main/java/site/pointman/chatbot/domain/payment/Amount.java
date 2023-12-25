package site.pointman.chatbot.domain.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class Amount {

    private int total;
    @JsonProperty("tax_free")
    private int taxFree;
    private int vat;
    private int point;
    private int discount;
    @JsonProperty("green_deposit")
    private int greenDeposit;

    /**
     * total	Integer	전체 결제 금액
     * tax_free	Integer	비과세 금액
     * vat	Integer	부가세 금액
     * point	Integer	사용한 포인트 금액
     * discount	Integer	할인 금액
     * green_deposit	Integer	컵 보증금
     */

    @Builder
    public Amount(int total, int taxFree, int vat, int point, int discount, int greenDeposit) {
        this.total = total;
        this.taxFree = taxFree;
        this.vat = vat;
        this.point = point;
        this.discount = discount;
        this.greenDeposit = greenDeposit;
    }

}
