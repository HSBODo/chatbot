package site.pointman.chatbot.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.customer.Member;
import site.pointman.chatbot.utill.StringUtils;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "tb_product")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private Member member;

    @Column(name = "buyer_user_key")
    private String buyerUserKey;

    private String reservation;
    private String name;
    private Long price;
    private String description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;

    @Convert(converter = CategoryEnumConverter.class)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToOne
    @JoinColumn(name = "product_images_id")
    private ProductImage productImages;

    @Builder
    public Product(Long id, Member member, String buyerUserKey, String reservation, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, Category category, ProductStatus status) {
        this.id = id;
        this.member = member;
        this.buyerUserKey = buyerUserKey;
        this.reservation = reservation;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
        this.category = category;
        this.status = status;
    }

    public void changeProductImage(ProductImage productImage){
        this.productImages = productImage;
    }
    public void changeStatus(ProductStatus productStatus){
        this.status = productStatus;
    }
    public String getProductDetailDescription(){
        return StringUtils.formatProductDetail(
                status,
                category.getValue(),
                StringUtils.formatPrice(price),
                description,
                tradingLocation,
                kakaoOpenChatUrl,
                getFormatCreateDate()
        );
    }

    public String getFormatCreateDate() {
        String createDate = getCreateDate();
        return StringUtils.dateFormat(createDate, "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
    }
}
