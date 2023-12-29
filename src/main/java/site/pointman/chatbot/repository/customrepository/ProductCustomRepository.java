package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;

import java.util.List;
import java.util.Optional;

public interface ProductCustomRepository {
    void saveProduct(Product product);
    void saveProductImage(ProductImage productImage);
    void insertProduct(ProductDto productDto, ProductImageDto productImageDto);
    void updateStatus(Long productId, ProductStatus productStatus, boolean isUse);
    void deleteProduct(Long productId, boolean isUse);
    List<Product> findByUserKey(String userKey, boolean isUse);
    List<Product> findByUserKey(String userKey, ProductStatus status, boolean isUse);
    List<Product> findByCategory(Category category, ProductStatus status, boolean isUse);
    List<Product> findByCategory(Category category, ProductStatus firstStatus,ProductStatus secondStatus, boolean isUse);
    Optional<Product> findByProductId(Long productId, boolean isUse);
    List<Product> findByAll();
    List<Product> findByStatus(ProductStatus firstStatus, ProductStatus secondStatus, boolean isUse);
    List<Product> findByStatus(ProductStatus firstStatus, boolean isUse);

}
