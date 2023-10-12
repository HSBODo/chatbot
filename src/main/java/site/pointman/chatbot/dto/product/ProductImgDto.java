package site.pointman.chatbot.dto.product;

import lombok.Getter;

@Getter
public class ProductImgDto {
    private OriginProduct originProduct;

    public String getImageUrl(){
        return originProduct.getImages().getRepresentativeImage().getUrl();
    }
}
