package site.pointman.chatbot.dto.product;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.product.ProductImage;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ProductImageDto {
    private List<String> imageUrl = new ArrayList<>();

    public ProductImage toEntity(){
        return ProductImage.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
