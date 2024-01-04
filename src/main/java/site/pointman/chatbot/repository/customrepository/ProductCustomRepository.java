package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.constant.product.Category;
import site.pointman.chatbot.constant.product.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;

import java.util.List;
import java.util.Optional;

public interface ProductCustomRepository {
    void saveProduct(Product product);
    void saveProductImage(ProductImage productImage);
    void insertProduct(ProductDto productDto, ProductImageDto productImageDto);
    void updateStatus(Long productId, ProductStatus productStatus, boolean isUse);
    void deleteProduct(Long productId, boolean isUse);
    List<Product> findByUserKey(String userKey, boolean isUse);
    Optional<Product> findByProductId(Long productId, boolean isUse);
    List<Product> findByAll();


}
