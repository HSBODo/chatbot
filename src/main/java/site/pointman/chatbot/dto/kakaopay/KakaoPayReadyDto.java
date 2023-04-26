package site.pointman.chatbot.dto.kakaopay;

import lombok.*;

@Getter
@Setter
@Builder
public class KakaoPayReadyDto {
    private String kakaoUserkey;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private int quantity;
    private int total_amount;
    private int vat_amount;
    private int tax_free_amount;
    private String approval_url;
    private String fail_url;
    private String cancel_url;
    private Long item_code;

}
