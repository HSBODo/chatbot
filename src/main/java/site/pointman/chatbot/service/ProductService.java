package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
    HttpResponse addProduct(ProductDto productDto, String userKey, List<String> imageUrls);
    HttpResponse getProductsByCategory(Category category);
    HttpResponse getProduct(Long productId);
    HttpResponse getProductsAll();
    HttpResponse getMemberProducts(String userKey);
    HttpResponse getMemberProductsByStatus(String userKey, ProductStatus productStatus);
    HttpResponse getMainProducts();
    HttpResponse getProductsBySearchWord(String searchWord);
    HttpResponse getSalesContractProducts(String userKey);
    HttpResponse getSalesContractProduct(String userKey,Long orderId);
    HttpResponse getPurchaseProducts(String userKey);
    HttpResponse getPurchaseProduct(String userKey,String orderId);
    HttpResponse updateProductStatus(Long productId, ProductStatus status);
    HttpResponse deleteProduct(Long productId);

//    ChatBotResponse getSpecialProducts(int currentPage,int firstNumber);
}
