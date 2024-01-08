package site.pointman.chatbot.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.constant.PayMethod;
import site.pointman.chatbot.domain.payment.kakaopay.request.KakaoPaymentCancelRequest;
import site.pointman.chatbot.domain.product.Product;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "tb_payment_info")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class PaymentInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Long orderId;

    private String tid;

    private String aid;

    private String cid;

    @Enumerated(EnumType.STRING)
    private PayMethod paymentMethodType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyerMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id")
    private Product product;

    private int quantity;

    private int taxFreeAmount;

    private int vatAmount;

    private String approvedAt;

    private String canceledAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Embedded
    private CardInfo cardInfo;

    @Embedded
    private Amount amountInfo;

    @Builder
    public PaymentInfo(Long orderId, String tid, String aid, String cid, PayMethod paymentMethodType, Member buyerMember, Product product, int quantity, int taxFreeAmount, int vatAmount, String approvedAt, String canceledAt, PaymentStatus status, CardInfo cardInfo, Amount amountInfo) {
        this.orderId = orderId;
        this.tid = tid;
        this.aid = aid;
        this.cid = cid;
        this.paymentMethodType = paymentMethodType;
        this.buyerMember = buyerMember;
        this.product = product;
        this.quantity = quantity;
        this.taxFreeAmount = taxFreeAmount;
        this.vatAmount = vatAmount;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.status = status;
        this.cardInfo = cardInfo;
        this.amountInfo = amountInfo;
    }

    public static PaymentInfo createPaymentReady(Long orderId, String cid, String tid, Member buyerMember, Product product,int taxFreeAmount, int vatAmount, int quantity){
        final PaymentStatus paymentStatus = PaymentStatus.결제준비;
        return PaymentInfo.builder()
                .orderId(orderId)
                .cid(cid)
                .tid(tid)
                .buyerMember(buyerMember)
                .product(product)
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .quantity(quantity)
                .status(paymentStatus)
                .build();
    }

    public void successPayment(String aid, String approvedAt, PayMethod paymentMethodType, CardInfo cardInfo, Amount amountInfo){
        this.status = PaymentStatus.결제완료;
        this.aid = aid;
        this.approvedAt = approvedAt;
        this.paymentMethodType = paymentMethodType;
        this.cardInfo = cardInfo;
        this.amountInfo = amountInfo;

    }

    public KakaoPaymentCancelRequest getKakaoPaymentCancelRequest () {
        return new KakaoPaymentCancelRequest(
                cid,
                tid,
                product.getPrice().intValue(),
                taxFreeAmount,
                vatAmount,
                product.getPrice().intValue()
        );
    }

    public void cancelPayment(String canceledAt) {
        this.status = PaymentStatus.결제취소;
        this.canceledAt = canceledAt;
    }


}
