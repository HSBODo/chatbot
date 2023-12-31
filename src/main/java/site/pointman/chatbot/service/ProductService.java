package site.pointman.chatbot.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.constant.product.Category;
import site.pointman.chatbot.constant.product.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Response addProduct(ProductDto productDto, String userKey, List<String> imageUrls);
    Response updateProductStatus(Long productId, ProductStatus status);
    Response deleteProduct(Long productId);
    Page<Product> getProductsByCategory(Category category, int pageNumber);
    Page<Product> getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber);
    Page<Product> getMainProducts(int pageNumber);
    Page<Product> getProductsBySearchWord(String searchWord, int pageNumber);
    Page<Product> getSalesContractProducts(String userKey, int pageNumber);
    Page<Order> getPurchaseProducts(String userKey, int pageNumber);
    List<Product> getProductsAll();
    List<Product> getMemberProducts(String userKey);
    Optional<Product> getProduct(Long productId);
    Optional<Order> getSalesContractProduct(String userKey, Long orderId);
    Optional<Order> getPurchaseProduct(String userKey, Long orderId);

}
