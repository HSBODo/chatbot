package site.pointman.chatbot.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductListDto {
    @JsonProperty("contents")
    private List<Content> productListDto;

    public ProductDto getProduct(int num){
        return productListDto.get(num).getChannelProducts().get(0);
    }
}

