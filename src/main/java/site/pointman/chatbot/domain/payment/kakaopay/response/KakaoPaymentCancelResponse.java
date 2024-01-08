package site.pointman.chatbot.domain.payment.kakaopay.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import site.pointman.chatbot.domain.payment.Amount;
import site.pointman.chatbot.domain.payment.CardInfo;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class KakaoPaymentCancelResponse {
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

    private int quantity;

    private Amount amount;

    @JsonProperty("card_info")
    private CardInfo cardInfo;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("approved_at")
    private String approvedAt;

    @JsonProperty("canceled_at")
    private String canceledAt;

}
