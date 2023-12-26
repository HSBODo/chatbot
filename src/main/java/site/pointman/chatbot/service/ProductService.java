package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
    HttpResponse getProductsByCategory(Category category);
    HttpResponse getMyProducts(String userKey, ProductStatus productStatus);
    HttpResponse getProductProfile(Long productId);
    HttpResponse getProductsBySearchWord(String searchWord);
    HttpResponse addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    HttpResponse updateProductStatus(String productId, String utterance);
    HttpResponse deleteProduct(String productId, String utterance);
    HttpResponse getContractProducts(String userKey);
    HttpResponse getContractProductProfile(String userKey,String orderId);
//    ChatBotResponse getSpecialProducts(int currentPage,int firstNumber);
    HttpResponse getMainProducts();
    Object getProducts();
    Object getProducts(String userKey);
    Object getProduct(Long productId);
    Object updateProductStatus(Long productId, ProductStatus status);


}
