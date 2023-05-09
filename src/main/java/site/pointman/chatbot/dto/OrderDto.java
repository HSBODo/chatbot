package site.pointman.chatbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Long idx ;
    private String tid;
    private Long order_id;
    private PayMethod payment_method_type;
    private String kakao_userkey;
    private String approved_at;
    private String canceled_at;
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
    private OrderStatus status;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;

    @Builder
    public OrderDto(Long idx, String tid, Long order_id, PayMethod payment_method_type, String kakao_userkey, String approved_at, String canceled_at, String aid, String cid, String partner_order_id, String partner_user_id, String item_name, Long item_code, int quantity, int total_amount, int tax_free_amount, int vat_amount, OrderStatus status, String next_redirect_app_url, String next_redirect_mobile_url, String next_redirect_pc_url, String android_app_scheme, String ios_app_scheme) {
        this.idx = idx;
        this.tid = tid;
        this.order_id = order_id;
        this.payment_method_type = payment_method_type;
        this.kakao_userkey = kakao_userkey;
        this.approved_at = approved_at;
        this.canceled_at = canceled_at;
        this.aid = aid;
        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.item_code = item_code;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.tax_free_amount = tax_free_amount;
        this.vat_amount = vat_amount;
        this.status = status;
        this.next_redirect_app_url = next_redirect_app_url;
        this.next_redirect_mobile_url = next_redirect_mobile_url;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.android_app_scheme = android_app_scheme;
        this.ios_app_scheme = ios_app_scheme;
    }
    public Order toEntity(){
        return Order.builder()
                .aid(this.aid)
                .tid(this.tid)
                .cid(this.cid)
                .kakao_userkey(this.kakao_userkey)
                .order_id(this.order_id)
                .item_code(this.item_code)
                .item_name(this.item_name)
                .quantity(this.quantity)
                .status(this.status)
                .payment_method_type(this.payment_method_type)
                .total_amount(this.total_amount)
                .vat_amount(this.vat_amount)
                .tax_free_amount(this.tax_free_amount)
                .next_redirect_app_url(this.next_redirect_app_url)
                .next_redirect_mobile_url(this.next_redirect_mobile_url)
                .next_redirect_pc_url(this.next_redirect_pc_url)
                .android_app_scheme(this.android_app_scheme)
                .ios_app_scheme(this.ios_app_scheme)
                .approved_at(this.approved_at)
                .canceled_at(this.canceled_at)
                .partner_order_id(this.partner_order_id)
                .partner_user_id(this.partner_user_id)
                .build();
    }


}
