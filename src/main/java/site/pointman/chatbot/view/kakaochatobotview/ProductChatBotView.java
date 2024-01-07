package site.pointman.chatbot.view.kakaochatobotview;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductDto;

import java.util.List;

public interface ProductChatBotView {

    ChatBotResponse updateProductStatusResultPage(ProductStatus productStatus);
    ChatBotResponse deleteProductResultPage();
    ChatBotResponse addProductReconfirmPage();
    ChatBotResponse productDetailInfoPage(String userKey, String productId);
    ChatBotResponse ProductListBySearchWordPage(Page<Product> products, String searchWord, int pageNumber);
    ChatBotResponse productListByCategoryPage(Page<Product> products, Category productCategory,int pageNumber);
    ChatBotResponse myProductListByStatusPage(Page<Product> productPage, ProductStatus status, int pageNumber);
    ChatBotResponse addProductInfoPreviewPage(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl);
    ChatBotResponse addProductResultPage();
    ChatBotResponse productCategoryListPage(String requestBlockId);
    ChatBotResponse mySalesContractProductListPage(String userKey, int pageNumber);
    ChatBotResponse mySalesContractProductOrderDetailInfoPage(Order salesContractProductOrder);
    ChatBotResponse myPurchaseProductOrderListPage(Page<Order> purchaseProductOrders, int pageNumber);
    ChatBotResponse myPurchaseProductOrderDetailInfoPage(Order purchaseProductOrder);
    ChatBotResponse specialProductListPage(int pageNumber, int firstNumber);
    ChatBotResponse mainSaleProductListPage(Page<Product> productPage,int currentPage);
}
