package site.pointman.chatbot.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.constant.product.Category;
import site.pointman.chatbot.constant.product.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> imageUrls = new ArrayList<>();
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
