package site.pointman.chatbot.dto.product;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ProductImageDto {
    private Product product ;
    private List<String> imageUrl = new ArrayList<>();

    public ProductImage toEntity(){
        return ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .build();
    }
}
