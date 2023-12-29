package site.pointman.chatbot.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Response addProduct(ProductDto productDto, String userKey, List<String> imageUrls);
    Page<Product> getProductsByCategory(Category category, int pageNumber);
    Optional<Product> getProduct(Long productId);
    List<Product> getProductsAll();
    List<Product> getMemberProducts(String userKey);
    Page<Product> getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber);
    Page<Product> getMainProducts(int pageNumber);
    Page<Product> getProductsBySearchWord(String searchWord, int pageNumber);
    Page<Product> getSalesContractProducts(String userKey, int pageNumber);
    Optional<Order> getSalesContractProduct(String userKey, Long orderId);
    Page<Order> getPurchaseProducts(String userKey, int pageNumber);
    Optional<Order> getPurchaseProduct(String userKey, Long orderId);
    Response updateProductStatus(Long productId, ProductStatus status);
    Response deleteProduct(Long productId);
}
