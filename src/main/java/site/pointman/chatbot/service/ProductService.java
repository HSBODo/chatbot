package site.pointman.chatbot.service;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductService {

    Object verificationCustomerSuccessResponse();
    Object getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category);
    Object getProductCategory(String requestBlockId);
    Object getProductsByCategory(Category category);
    Object getMyProducts(String userKey, String productStatus);
    Object getProductProfile(String productId, String userKey);
    Object getProductsBySearchWord(String searchWord);
    Object addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory);
    Object updateProductStatus(String productId, String utterance);
    Object deleteProduct(String productId, String utterance);
    Object getContractProducts(String userKey);
    Object getContractProductProfile(String userKey,String orderId);


}
