package site.pointman.chatbot.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id ;
    private String userKey;
    private String name;
    private Long price;
    private String description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;
    private Category category;
    private List<String> imageUrls = new ArrayList<>();
    private ProductStatus status;

    @Builder
    public ProductDto(Long id, String userKey, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, Category category, ProductStatus status, List<String> imageUrls) {
        this.id = id;
        this.userKey = userKey;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
        this.category = category;
        this.status = status;
        this.imageUrls = imageUrls;
    }
}
