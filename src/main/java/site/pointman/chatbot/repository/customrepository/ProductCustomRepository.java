package site.pointman.chatbot.repository.customrepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductCustomRepository {
    void saveProduct(Product product);
    List<Product> findByUserKey(String userKey, boolean isUse);
    Optional<Product> findByProductId(Long productId, boolean isUse);
    Page<Product> findAll(ProductCondition productCondition, Pageable pageable);
    Page<ProductDto> findDtoAll(ProductCondition productCondition, Pageable pageable);
}
