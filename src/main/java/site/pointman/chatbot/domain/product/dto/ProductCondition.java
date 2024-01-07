package site.pointman.chatbot.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;

@Getter
public class ProductCondition {
    private Category productCategory;
    private ProductStatus firstProductStatus;
    private ProductStatus secondProductStatus;
    private String searchWord;
    private String userKey;

    @Builder
    public ProductCondition(Category productCategory, ProductStatus firstProductStatus, ProductStatus secondProductStatus, String searchWord, String userKey) {
        this.productCategory = productCategory;
        this.firstProductStatus = firstProductStatus;
        this.secondProductStatus = secondProductStatus;
        this.searchWord = searchWord;
        this.userKey = userKey;
    }
}
