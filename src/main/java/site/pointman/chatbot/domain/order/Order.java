package site.pointman.chatbot.domain.order;

import com.mysql.cj.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.order.constatnt.OrderMemberConfirmStatus;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.utill.CustomStringUtils;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "tb_order")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyerMember;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id")
    private Product product;

    private int quantity;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private OrderMemberConfirmStatus buyerConfirmStatus = OrderMemberConfirmStatus.미확정;

    @Enumerated(EnumType.STRING)
    private OrderMemberConfirmStatus sellerConfirmStatus = OrderMemberConfirmStatus.미확정;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private PaymentInfo paymentInfo;

    @Builder
    public Order(Long orderId, Member buyerMember, Product product, int quantity, PaymentInfo paymentInfo, String trackingNumber,OrderStatus status) {
        this.orderId = orderId;
        this.buyerMember = buyerMember;
        this.product = product;
        this.quantity = quantity;
        this.paymentInfo = paymentInfo;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public static Order createOrder(Long orderId, Member buyerMember, Product product, PaymentInfo approvePaymentInfo) {
        final int quantity = 1;
        product.changeStatus(ProductStatus.판매대기);
        return Order.builder()
                .status(OrderStatus.주문체결)
                .orderId(orderId)
                .buyerMember(buyerMember)
                .product(product)
                .quantity(quantity)
                .paymentInfo(approvePaymentInfo)
                .build();

    }

    public void orderCancel(){
        status = OrderStatus.주문취소;
        product.changeStatus(ProductStatus.판매중);
    }


    public void orderSuccessConfirm(){
        this.status = OrderStatus.거래완료;
        this.product.changeStatus(ProductStatus.판매완료);
        this.product.orderSuccessBuyerMemberUserKey(buyerMember.getUserKey());
    }

    public boolean isConfirm(){
        if (buyerConfirmStatus.equals(OrderMemberConfirmStatus.구매확정) && sellerConfirmStatus.equals(OrderMemberConfirmStatus.판매확정)) return true;
        return false;
    }

    public void changeTrackingNumber(String trackingNumber){
        this.trackingNumber = trackingNumber;
    }

    public void buyerConfirmStatus() {
        this.buyerConfirmStatus = OrderMemberConfirmStatus.구매확정;
    }

    public void sellerConfirmStatus() {
        this.sellerConfirmStatus = OrderMemberConfirmStatus.판매확정;
    }

    public String viewTackingNumber(){
        if (StringUtils.isNullOrEmpty(trackingNumber)) return "미입력";
        return this.trackingNumber;
    }


    public String getFormatApproveDate(){
       return paymentInfo.getFormatCreateDate();
    }

    public boolean isTrading(){
        if (status.equals(OrderStatus.주문체결)) return  true;
        return false;
    }

    public boolean isInputTrackingNumber(){
        if(StringUtils.isNullOrEmpty(trackingNumber)) return false;
        return true;
    }

    public String getPurchaseProductProfile(){
        StringBuilder productProfile = new StringBuilder();
        String formatPrice = CustomStringUtils.formatPrice(product.getPrice());

        return productProfile
                .append("상품상태: " + product.getStatus().getOppositeValue())
                .append("\n")
                .append("주문번호: " + orderId)
                .append("\n\n")
                .append("판매자: " + product.getMember().getName())
                .append("\n")
                .append("카테고리: " + product.getCategory().getValue())
                .append("\n\n")
                .append("판매가격: " + formatPrice+"원")
                .append("\n\n")
                .append("상품 설명: " + product.getDescription())
                .append("\n\n")
                .append("거래 희망 장소: " + product.getTradingLocation())
                .append("\n\n")
                .append("카카오 오픈 채팅방: " + product.getKakaoOpenChatUrl())
                .append("\n")
                .append("운송장번호: " + viewTackingNumber())
                .append("\n\n")
                .append("결제일자: " + getFormatApproveDate())
                .toString();
    }

}
