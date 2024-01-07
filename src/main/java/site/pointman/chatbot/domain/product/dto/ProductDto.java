package site.pointman.chatbot.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;

import java.time.LocalDateTime;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id ;
    private String userKey;
    private String memberName;
    private String name;
    private Long price;
    private String description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;
    private Category category;
    private List<String> imageUrls;
    private ProductStatus status;
    private LocalDateTime createDate;

    @Builder
    public ProductDto(Long id, String userKey, String memberName, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, Category category, List<String> imageUrls, ProductStatus status, LocalDateTime createDate) {
        this.id = id;
        this.userKey = userKey;
        this.memberName = memberName;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
        this.category = category;
        this.imageUrls = imageUrls;
        this.status = status;
        this.createDate = createDate;
    }
}
