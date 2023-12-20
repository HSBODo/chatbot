package site.pointman.chatbot.domain.payment.kakaopay;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class PaymentApproveRequest {
    private String cid;
    private String tid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;
    private int totalAmount;

    @Builder
    public PaymentApproveRequest(String cid, String tid, String partnerOrderId, String partnerUserId, String pgToken, int totalAmount) {
        this.cid = cid;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
        this.totalAmount = totalAmount;
    }

    public MultiValueMap<String, String> getConvertRequestEntity(){
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("cid", cid);
        requestEntity.add("tid", tid);
        requestEntity.add("partner_order_id", partnerOrderId);
        requestEntity.add("partner_user_id", partnerUserId);
        requestEntity.add("pg_token", pgToken);
        requestEntity.add("total_amount", String.valueOf(totalAmount));

        return requestEntity;
    }
}
