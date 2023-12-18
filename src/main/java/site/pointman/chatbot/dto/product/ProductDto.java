package site.pointman.chatbot.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Member;
import site.pointman.chatbot.domain.product.Product;

@Getter
@Setter
public class ProductDto {
    private Long id ;
    private Member member;
    private String name;
    private Long price;
    private String description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;
    private Category category;
    private ProductStatus status;

    @Builder
    public ProductDto(Long id, Member member, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, Category category, ProductStatus status) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
        this.category = category;
        this.status = status;
    }

    public Product toEntity(){
        return Product.builder()
                .id(id)
                .member(member)
                .name(name)
                .price(price)
                .description(description)
                .tradingLocation(tradingLocation)
                .kakaoOpenChatUrl(kakaoOpenChatUrl)
                .category(category)
                .status(status)
                .build();
    }
}
