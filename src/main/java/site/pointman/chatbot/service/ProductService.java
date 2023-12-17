package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

import java.util.List;

public interface ProductService {

    ChatBotResponse verificationCustomerSuccessResponse();
    ChatBotResponse getProductCategory(String requestBlockId);
    ChatBotResponse getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    ChatBotResponse getProductsByUserKey(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductProfile(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductsByCategory(ChatBotRequest chatBotRequest);
    ChatBotResponse addProduct(ChatBotRequest chatBotRequest);
    ChatBotResponse updateProductStatus(ChatBotRequest chatBotRequest);
    ChatBotResponse deleteProduct(ChatBotRequest chatBotRequest);
}
