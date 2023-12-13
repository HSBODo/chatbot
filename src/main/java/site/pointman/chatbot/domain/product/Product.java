package site.pointman.chatbot.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.customer.Customer;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "tb_product")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id ;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private Customer customer;

    @Column(name = "buyer_user_key")
    private String buyerUserKey;

    private String reservation;
    private String name;
    private Long price;
    private String Description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToOne(mappedBy = "product")
    private ProductImage productImages;

    @Builder
    public Product(Long id, Customer customer, String buyerUserKey, String reservation, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, ProductStatus status) {
        this.Id = id;
        this.customer = customer;
        this.buyerUserKey = buyerUserKey;
        this.reservation = reservation;
        this.name = name;
        this.price = price;
        this.Description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
        this.status = status;
    }




}
