package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {
    ChatBotResponse verificationCustomerSuccessResponse();
    ChatBotResponse getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    ChatBotResponse getProductCategory(String requestBlockId);
    ChatBotResponse getProductsByCategory(Category category);
    ChatBotResponse getMyProducts(String userKey, String productStatus);
    ChatBotResponse getProductProfile(String productId, String userKey);
    ChatBotResponse getProductsBySearchWord(String searchWord);
    ChatBotResponse addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    ChatBotResponse updateProductStatus(String productId, String utterance);
    ChatBotResponse deleteProduct(String productId, String utterance);
    ChatBotResponse getContractProducts(String userKey);
    ChatBotResponse getContractProductProfile(String userKey,String orderId);
    ChatBotResponse getSpecialProducts(int currentPage,int firstNumber);
    Object getProducts();
    Object getProducts(String userKey);
    Object getProduct(Long productId);
    Object updateProductStatus(Long productId, ProductStatus status);


}
