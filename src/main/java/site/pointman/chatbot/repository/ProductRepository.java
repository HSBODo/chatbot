package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void saveProduct(Product product);
    void saveProductImage(ProductImage productImage);
    void insertProduct(ProductDto productDto, ProductImageDto productImageDto);
    void updateStatus(Long productId, ProductStatus productStatus);
    void deleteProduct(Long productId);
    List<Product> findByUserKey(String userKey);
    List<Product> findByCategory(Category category, ProductStatus status);
    Optional<Product> findByProductId(Long productId);

}
