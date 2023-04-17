package site.pointman.chatbot.domain.kakaopay;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@Table(name = "tb_kakao_pay")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class KakaoPay extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;

    private String tid;
    @Id
    private Long order_id;

    private String payment_method_type;
    private String kakao_userkey;
    private String approved_at;
    private String aid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private Long item_code;
    private int quantity;
    private int total_amount;
    private int tax_free_amount;
    private int vat_amount;
    private String status;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;
}
