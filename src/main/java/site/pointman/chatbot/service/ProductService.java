package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
    HttpResponse addProduct(ProductDto productDto, String userKey, List<String> imageUrls);
    HttpResponse getProductsByCategory(Category category, int pageNumber);
    HttpResponse getProduct(Long productId);
    HttpResponse getProductsAll();
    HttpResponse getMemberProducts(String userKey);
    HttpResponse getMemberProductsByStatus(String userKey, ProductStatus productStatus, int pageNumber);
    HttpResponse getMainProducts(int page);
    HttpResponse getProductsBySearchWord(String searchWord, int pageNumber);
    HttpResponse getSalesContractProducts(String userKey, int pageNumber);
    HttpResponse getSalesContractProduct(String userKey,Long orderId);
    HttpResponse getPurchaseProducts(String userKey, int pageNumber);
    HttpResponse getPurchaseProduct(String userKey,String orderId);
    HttpResponse updateProductStatus(Long productId, ProductStatus status);
    HttpResponse deleteProduct(Long productId);

//    ChatBotResponse getSpecialProducts(int currentPage,int firstNumber);
}
