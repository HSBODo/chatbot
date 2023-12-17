package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {

    ChatBotResponse verificationCustomerSuccessResponse();
    ChatBotResponse getProductCategory(String requestBlockId);
    ChatBotResponse getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    ChatBotResponse getProductsByUserKey(String userKey);
    ChatBotResponse getProductProfile(String productId, String userKey);
    ChatBotResponse getProductsByCategory(Category category);
    ChatBotResponse addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    ChatBotResponse updateProductStatus(String productId, String utterance);
    ChatBotResponse deleteProduct(String productId, String utterance);
}
