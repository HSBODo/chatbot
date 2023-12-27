package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.SpecialProduct;

import java.util.List;

public interface ProductChatBotResponseService {

    ChatBotResponse updateStatusChatBotResponse(String productId, String utterance);
    ChatBotResponse deleteProductChatBotResponse(String productId, String utterance);
    ChatBotResponse verificationCustomerSuccessChatBotResponse();
    ChatBotResponse getProductChatBotResponse(String userKey, String productId);
    ChatBotResponse searchProductsChatBotResponse(String searchWord);
    ChatBotResponse getProductsByCategoryChatBotResponse(Category category);
    ChatBotResponse createMyProductsByStatusChatBotResponse(String userKey, String productStatus);
    ChatBotResponse getProductInfoPreviewChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductChatBotResponse(ProductDto productDto, String userKey, List<String> imageUrls);
    ChatBotResponse getCategoryChatBotResponse(String requestBlockId);
    ChatBotResponse getSalesContractProductsChatBotResponse(String userKey);
    ChatBotResponse getSalesContractProductChatBotResponse(String userKey, String orderId);
    ChatBotResponse getPurchaseProducts(String userKey);
    ChatBotResponse getPurchaseProductProfile(String userKey,String orderId);
    ChatBotResponse getSpecialProductsChatBotResponse(int nextFirstNumber, int nextPage);
    ChatBotResponse getMainProductsChatBotResponse(int currentPage);
}
