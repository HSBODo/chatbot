package site.pointman.chatbot.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;

@Getter
@Setter
public class ProductDto {
    private Long id ;
    private Customer customer;
    private String name;
    private Long price;
    private String description;
    private String tradingLocation;
    private String kakaoOpenChatUrl;
    private ProductStatus status;

    @Builder
    public ProductDto(Customer customer, String name, Long price, String description, String tradingLocation, String kakaoOpenChatUrl) {
        this.customer = customer;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
    }

    public Product toEntity(){
        return Product.builder()
                .id(id)
                .customer(customer)
                .name(name)
                .price(price)
                .description(description)
                .tradingLocation(tradingLocation)
                .kakaoOpenChatUrl(kakaoOpenChatUrl)
                .status(status)
                .build();
    }
}
