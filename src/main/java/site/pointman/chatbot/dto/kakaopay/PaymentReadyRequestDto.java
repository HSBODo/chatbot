package site.pointman.chatbot.dto.kakaopay;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentReadyRequestDto {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAmount;
    private String approvalUrl;
    private String failUrl;
    private String cancelUrl;

    @Builder
    public PaymentReadyRequestDto(String cid, String partnerOrderId, String partnerUserId, String itemName, int quantity, int totalAmount, int taxFreeAmount, String approvalUrl, String failUrl, String cancelUrl) {
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.approvalUrl = approvalUrl;
        this.failUrl = failUrl;
        this.cancelUrl = cancelUrl;
    }

}
