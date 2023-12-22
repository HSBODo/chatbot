package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotResponse;

import java.util.List;

public interface ProductChatBotResponseService {

    ChatBotResponse updateStatusSuccessChatBotResponse(ProductStatus productStatus);
    ChatBotResponse deleteProductSuccessChatBotResponse();
    ChatBotResponse verificationCustomerSuccessChatBotResponse();
    ChatBotResponse getProductProfileSuccessChatBotResponse(String buyerUserKey, Product product);
    ChatBotResponse createProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId);
    ChatBotResponse createMyProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId);
    ChatBotResponse getProductInfoPreviewSuccessChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductSuccessChatBotResponse();
    ChatBotResponse getCategoryChatBotResponse(BlockId nextBlockId);
    ChatBotResponse getContractProductsSuccessChatBotResponse(List<Product> contractProducts);
    ChatBotResponse getContractProductProfileSuccessChatBotResponse(Order order);
}
