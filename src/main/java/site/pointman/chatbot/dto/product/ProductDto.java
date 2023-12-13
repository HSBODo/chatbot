package site.pointman.chatbot.dto.product;

import lombok.Builder;
import lombok.Getter;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;

@Getter
public class ProductDto {
    private Long id ;
    private Customer customer;
    private String productName;
    private Long productPrice;
    private String productDescription;
    private String tradingLocation;
    private String kakaoOpenChatUrl;

    @Builder
    public ProductDto(Long id, Customer customer, String productName, Long productPrice, String productDescription, String tradingLocation, String kakaoOpenChatUrl) {
        this.id =id;
        this.customer = customer;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.tradingLocation = tradingLocation;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
    }

    public Product toEntity(){
        return Product.builder()
                .customer(customer)
                .name(productName)
                .price(productPrice)
                .description(productDescription)
                .tradingLocation(tradingLocation)
                .kakaoOpenChatUrl(kakaoOpenChatUrl)
                .build();
    }
}
