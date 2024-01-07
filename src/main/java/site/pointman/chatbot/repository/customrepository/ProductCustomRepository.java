package site.pointman.chatbot.repository.customrepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductCondition;

import java.util.List;
import java.util.Optional;

public interface ProductCustomRepository {
    void saveProduct(Product product);
    void saveProductImage(ProductImage productImage);
    void updateStatus(Long productId, ProductStatus productStatus, boolean isUse);
    List<Product> findByUserKey(String userKey, boolean isUse);
    Optional<Product> findByProductId(Long productId, boolean isUse);
    List<Product> findByAll();
    Page<Product> findAll(ProductCondition productCondition, Pageable pageable);
}
