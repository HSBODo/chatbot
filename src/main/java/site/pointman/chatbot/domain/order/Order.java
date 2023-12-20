package site.pointman.chatbot.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.constant.PaymentStatus;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;

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

    @ManyToOne
    private Member buyerMember;

    @ManyToOne
    @JoinColumn(name = "product_Id")
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne
    private PaymentInfo paymentInfo;

    @Builder
    public Order(Long orderId, Member buyerMember, Product product, int quantity, OrderStatus status) {
        this.orderId = orderId;
        this.buyerMember = buyerMember;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    public void changeStatus(OrderStatus status){
        this.status = status;
    }
}
