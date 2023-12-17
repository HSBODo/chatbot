package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface ProductService {

    ChatBotResponse validationCustomer(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductCategory(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductInfoPreview(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductsByUserKey(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductProfile(ChatBotRequest chatBotRequest);
    ChatBotResponse getProductsByCategory(ChatBotRequest chatBotRequest);
    ChatBotResponse addProduct(ChatBotRequest chatBotRequest);
    ChatBotResponse addCategory(ChatBotRequest chatBotRequest);
    ChatBotResponse updateProductStatus(ChatBotRequest chatBotRequest);
    ChatBotResponse deleteProduct(ChatBotRequest chatBotRequest);
}
