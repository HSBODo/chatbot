package site.pointman.chatbot.dto.kakaopay;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoPayReady {
    private String kakaoUserkey;
    private String cid="TC0ONETIME";
    private String partner_order_id="partner_order_id";
    private String partner_user_id="partner_user_id";
    private String item_name="초코파이";
    private int quantity=1;
    private int total_amount=2200;
    private int vat_amount=200;
    private int tax_free_amount=0;
    private String approval_url="https://www.pointman.shop/kakaochat/v1/kakaopay-approve";
    private String fail_url="https://www.pointman.shop/kakaochat/v1/kakaopay-approve";
    private String cancel_url="https://www.pointman.shop/kakaochat/v1/kakaopay-approve";
    private String item_code="123123";

}
