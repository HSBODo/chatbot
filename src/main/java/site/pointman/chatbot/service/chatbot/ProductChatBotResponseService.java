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

    ChatBotResponse updateStatusSuccessChatBotResponse(String productId, String utterance);
    ChatBotResponse deleteProductSuccessChatBotResponse(String productId, String utterance);
    ChatBotResponse verificationCustomerSuccessChatBotResponse();
    ChatBotResponse getProductProfileSuccessChatBotResponse(String userKey, String productId);
    ChatBotResponse createProductListChatBotResponse(String searchWord);
    ChatBotResponse createProductListChatBotResponse(Category category);
    ChatBotResponse createMyProductListChatBotResponse(String userKey, String productStatus);
    ChatBotResponse getProductInfoPreviewSuccessChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductSuccessChatBotResponse(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    ChatBotResponse getCategoryChatBotResponse(String requestBlockId);
    ChatBotResponse getContractProductsSuccessChatBotResponse(String userKey);
    ChatBotResponse getContractProductProfileSuccessChatBotResponse(String userKey, String orderId);
    ChatBotResponse getSpecialProductsSuccessChatBotResponse(int nextFirstNumber, int nextPage);
    ChatBotResponse getMainProductsChatBotResponse();
}
