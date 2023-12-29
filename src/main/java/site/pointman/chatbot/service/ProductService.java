package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
    Response addProduct(ProductDto productDto, String userKey, List<String> imageUrls);
    Response getProductsByCategory(Category category, int pageNumber);
    Response getProduct(Long productId);
    Response getProductsAll();
    Response getMemberProducts(String userKey);
    Response getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber);
    Response getMainProducts(int page);
    Response getProductsBySearchWord(String searchWord, int pageNumber);
    Response getSalesContractProducts(String userKey, int pageNumber);
    Response getSalesContractProduct(String userKey, Long orderId);
    Response getPurchaseProducts(String userKey, int pageNumber);
    Response getPurchaseProduct(String userKey, String orderId);
    Response updateProductStatus(Long productId, ProductStatus status);
    Response deleteProduct(Long productId);

//    ChatBotResponse getSpecialProducts(int currentPage,int firstNumber);
}
