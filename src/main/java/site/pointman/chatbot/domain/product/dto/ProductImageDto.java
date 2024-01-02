package site.pointman.chatbot.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.product.ProductImage;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ProductImageDto {
    private List<String> imageUrls = new ArrayList<>();

    public ProductImage toEntity(){
        return ProductImage.builder()
                .imageUrls(imageUrls)
                .build();
    }
}
