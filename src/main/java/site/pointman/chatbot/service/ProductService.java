package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {

    ChatBotResponse verificationCustomerSuccessResponse();
    ChatBotResponse getProductCategory(String requestBlockId);
    ChatBotResponse getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    ChatBotResponse getProductsByUserKey(String userKey);
    ChatBotResponse getProductProfile(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductsByCategory(Category category);
    ChatBotResponse addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, Category productCategory);
    ChatBotResponse updateProductStatus(ChatBotRequest chatBotRequest);
    ChatBotResponse deleteProduct(ChatBotRequest chatBotRequest);
}
