package site.pointman.chatbot.domain.payment.kakaopay;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class PaymentReadyRequest {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private int taxFreeAmount;
    private int vatAmount;
    private String approvalUrl;
    private String failUrl;
    private String cancelUrl;

    @Builder
    public PaymentReadyRequest(String cid, String partnerOrderId, String partnerUserId, String itemName, int quantity, int totalAmount, int taxFreeAmount, int vatAmount, String approvalUrl, String failUrl, String cancelUrl) {
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.vatAmount = vatAmount;
        this.approvalUrl = approvalUrl;
        this.failUrl = failUrl;
        this.cancelUrl = cancelUrl;
    }

    public MultiValueMap<String, String> getConvertRequestEntity(){
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("cid", cid);
        requestEntity.add("partner_order_id", partnerOrderId);
        requestEntity.add("partner_user_id", partnerUserId);
        requestEntity.add("item_name", itemName);
        requestEntity.add("quantity", String.valueOf(quantity));
        requestEntity.add("total_amount", String.valueOf(totalAmount));
        requestEntity.add("vat_amount",String.valueOf(vatAmount));
        requestEntity.add("tax_free_amount", String.valueOf(taxFreeAmount));
        requestEntity.add("approval_url", approvalUrl); // 성공 시 redirect url
        requestEntity.add("cancel_url", cancelUrl); // 취소 시 redirect url
        requestEntity.add("fail_url", failUrl); // 실패 시 redirect url
        return requestEntity;
    }

}
