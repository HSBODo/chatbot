package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;

public interface ProductRepository {
    void save(ProductDto productDto);
    void productImageSave(ProductImageDto productImageDto);

}
