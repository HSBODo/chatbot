package site.pointman.chatbot.domain.product.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.product.dto.ProductCondition;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void addProduct(ProductDto productDto);
    void updateProductStatus(Long productId, ProductStatus updateStatus);
    void deleteProduct(Long productId);
    Page<Product> getProducts(ProductCondition productCondition, int pageNumber);
    Page<Product> getSalesContractProducts(String userKey, int pageNumber);
    Optional<Order> getSalesContractProduct(String userKey, Long orderId);
    Page<Order> getPurchaseProducts(String userKey, int pageNumber);
    Optional<Order> getPurchaseProduct(String userKey, Long orderId);
    List<Product> getProductsAll();
    List<Product> getMemberProducts(String userKey);
    Optional<Product> getProduct(Long productId);

}
