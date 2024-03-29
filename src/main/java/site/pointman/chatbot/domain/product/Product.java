package site.pointman.chatbot.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.converter.CategoryEnumConverter;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.utill.CustomNumberUtils;
import site.pointman.chatbot.utill.CustomStringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "tb_product")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_images_id")
    private ProductImage productImages;

    @Builder
    public Product(Long id, Member member, String buyerUserKey, String reservation, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, Category category, ProductStatus status, ProductImage productImage) {
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
        this.productImages = productImage;
    }

    public static Product createProduct(ProductDto productDto,Member member,ProductImage productImage) {
        final Long productId = CustomNumberUtils.createNumberId();
        return Product.builder()
                .id(productId)
                .member(member)
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .tradingLocation(productDto.getTradingLocation())
                .kakaoOpenChatUrl(productDto.getKakaoOpenChatUrl())
                .category(productDto.getCategory())
                .status(ProductStatus.판매중)
                .productImage(productImage)
                .build();
    }

    public ProductDto toDto(){
        return ProductDto.builder()
                .id(id)
                .userKey(member.getUserKey())
                .memberName(member.getName())
                .name(name)
                .price(price)
                .description(description)
                .tradingLocation(tradingLocation)
                .kakaoOpenChatUrl(kakaoOpenChatUrl)
                .category(category)
                .imageUrls(productImages.getImageUrls())
                .status(status)
                .createDate(getCreateDate())
                .build();
    }

    public void changeStatus(ProductStatus productStatus){
        this.status = productStatus;
    }

    public void orderSuccessBuyerMemberUserKey(String buyerUserKey){
        this.buyerUserKey = buyerUserKey;
    }

    public String getProductProfileTypeOfChatBot(){
        StringBuilder productProfile = new StringBuilder();
        String formatPrice = CustomStringUtils.formatPrice(price);
        return productProfile
                .append("상품상태: " + status)
                .append("\n")
                .append("카테고리: " + category.getValue())
                .append("\n\n")
                .append("판매가격: " + formatPrice+"원")
                .append("\n\n")
                .append("상품 설명: " + description)
                .append("\n\n")
                .append("거래 희망 장소: " + tradingLocation)
                .append("\n\n")
                .append("카카오 오픈 채팅방: " + kakaoOpenChatUrl)
                .append("\n\n")
                .append("등록일자: " + getFormatCreateDate())
                .toString();
    }

    public String getFormatCreateDate() {
        return super.getFormatCreateDate();
    }
    public String getKakaoPaymentUrl(String buyerUserKey,String hostUrl){
        StringBuilder paymentUrl = new StringBuilder(hostUrl);
        paymentUrl.append("/order")
                .append("/kakaopay-ready")
                .append("/"+id)
                .append("?")
                .append("userKey="+buyerUserKey);
        return paymentUrl.toString();
    }

    public boolean isTrading(){
        if (status.equals(ProductStatus.판매대기)) return true;
        return false;
    }

    public boolean isAvailablePurchase(){
        if (status.equals(ProductStatus.판매중)) return true;
        return false;
    }

    public void deleteProduct(){
        super.delete();
        productImages.delete();
    }

}
