package site.pointman.chatbot.domain.payment.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import site.pointman.chatbot.domain.payment.Amount;
import site.pointman.chatbot.domain.payment.CardInfo;

import java.util.Objects;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class KakaoPaymentApproveResponse {
    private String aid;
    private String cid;
    private String tid;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;

    @JsonProperty("partner_user_id")
    private String partnerUserId;

    @JsonProperty("payment_method_type")
    private String paymentMethodType;

    @JsonProperty("item_name")
    private String itemName;

    private Amount amount;

    @JsonProperty("card_info")
    private CardInfo cardInfo;

    private int quantity;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("approved_at")
    private String approvedAt;

    /**
     * aid	String	요청 고유 번호
     * tid	String	결제 고유 번호
     * cid	String	가맹점 코드
     * sid	String	정기 결제용 ID, 정기 결제 CID로 단건 결제 요청 시 발급
     * partner_order_id	String	가맹점 주문번호, 최대 100자
     * partner_user_id	String	가맹점 회원 id, 최대 100자
     * payment_method_type	String	결제 수단, CARD 또는 MONEY 중 하나
     * amount	Amount	결제 금액 정보
     * card_info	CardInfo	결제 상세 정보, 결제수단이 카드일 경우만 포함
     * item_name	String	상품 이름, 최대 100자
     * item_code	String	상품 코드, 최대 100자
     * quantity	Integer	상품 수량
     * created_at	Datetime	결제 준비 요청 시각
     * approved_at	Datetime	결제 승인 시각
     * payload	String	결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
     */

    public Amount getAmount() {
        if(Objects.isNull(amount)) return null;
        return amount;
    }

    public CardInfo getCardInfo() {
        if(Objects.isNull(cardInfo)) return null;
        return cardInfo;
    }
}
