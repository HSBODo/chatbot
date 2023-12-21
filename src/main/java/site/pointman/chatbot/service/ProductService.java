package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {

    Response verificationCustomerSuccessResponse();
    Response getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    Response getProductCategory(String requestBlockId);
    Response getProductsByCategory(Category category);
    Response getMyProducts(String userKey, String productStatus);
    Response getProductProfile(String productId, String userKey);
    Response getProductsBySearchWord(String searchWord);
    Response addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    Response updateProductStatus(String productId, String utterance);
    Response deleteProduct(String productId, String utterance);
    Response getContractProducts(String userKey);
    Response getContractProductProfile(String userKey,String orderId);


}
