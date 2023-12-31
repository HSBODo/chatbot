package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;

public interface ProductChatBotView {

    ChatBotResponse updateProductStatusResultPage(String productId, String utterance);
    ChatBotResponse deleteProductResultPage(String productId, String utterance);
    ChatBotResponse addProductReconfirmPage();
    ChatBotResponse productDetailInfoPage(String userKey, String productId);
    ChatBotResponse ProductListBySearchWordPage(String searchWord, int pageNumber);
    ChatBotResponse productListByCategoryPage(Category category, int pageNumber);
    ChatBotResponse myProductListByStatusPage(String userKey, String productStatus, int pageNumber);
    ChatBotResponse addProductInfoPreviewPage(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductResultPage(ProductDto productDto, String userKey, List<String> imageUrls);
    ChatBotResponse productCategoryListPage(String requestBlockId);
    ChatBotResponse mySalesContractProductListPage(String userKey, int pageNumber);
    ChatBotResponse mySalesContractProductDetailInfoPage(String userKey, String orderId);
    ChatBotResponse myPurchaseProductListPage(String userKey,int pageNumber);
    ChatBotResponse myPurchaseProductDetailInfoPage(String userKey,String orderId);
    ChatBotResponse specialProductListPage(int pageNumber, int firstNumber);
    ChatBotResponse mainSaleProductListPage(int currentPage);
}
