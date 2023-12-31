package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductChatBotView {

    ChatBotResponse updateStatusChatBotResponse(String productId, String utterance);
    ChatBotResponse deleteProductChatBotResponse(String productId, String utterance);
    ChatBotResponse verificationCustomerSuccessChatBotResponse();
    ChatBotResponse getProductChatBotResponse(String userKey, String productId);
    ChatBotResponse searchProductsChatBotResponse(String searchWord, int pageNumber);
    ChatBotResponse getProductsByCategoryChatBotResponse(Category category, int pageNumber);
    ChatBotResponse getMyProductsByStatusChatBotResponse(String userKey, String productStatus, int pageNumber);
    ChatBotResponse getProductInfoPreviewChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductChatBotResponse(ProductDto productDto, String userKey, List<String> imageUrls);
    ChatBotResponse getCategoryChatBotResponse(String requestBlockId);
    ChatBotResponse getSalesContractProductsChatBotResponse(String userKey, int pageNumber);
    ChatBotResponse getSalesContractProductChatBotResponse(String userKey, String orderId);
    ChatBotResponse getPurchaseProducts(String userKey,int pageNumber);
    ChatBotResponse getPurchaseProductProfile(String userKey,String orderId);
    ChatBotResponse getSpecialProductsChatBotResponse(int pageNumber, int firstNumber);
    ChatBotResponse getMainProductsChatBotResponse(int currentPage);
}
