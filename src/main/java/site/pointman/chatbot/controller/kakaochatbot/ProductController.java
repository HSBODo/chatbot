package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.handler.annotation.ValidateMember;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.view.kakaochatobotview.ProductChatBotView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {


    ProductChatBotView productChatBotView;
    MemberService memberService;

    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public ProductController(ProductChatBotView productChatBotView, MemberService memberService) {
        this.productChatBotView = productChatBotView;
        this.memberService = memberService;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "POST/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return productChatBotView.addProductReconfirmPage();
    }

    @ResponseBody
    @PostMapping(value = "GET/category" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        String requestBlockId = chatBotRequest.getRequestBlockId();

        return productChatBotView.productCategoryListPage(requestBlockId);
    }

    @ResponseBody
    @PostMapping(value = "GET/preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) {
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productName = chatBotRequest.getProductName();
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
        String category = chatBotRequest.getChoiceParam();

        return productChatBotView.addProductInfoPreviewPage(imageUrls,category,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl);
    }

    @ResponseBody
    @PostMapping(value = "GET/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.productListByCategoryPage(category,pageNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "POST" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest){
        String userKey = chatBotRequest.getUserKey();
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productCategory = chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue();
        ProductDto productDto = chatBotRequest.createProductDto();
        Category category = Category.getCategory(productCategory);
        productDto.setCategory(category);
        productDto.setImageUrls(imageUrls);
        return productChatBotView.addProductResultPage(
                productDto,
                userKey);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/myProductsByStatus" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productStatus = chatBotRequest.getProductStatus();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.myProductListByStatusPage(userKey,productStatus,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/mainProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMainProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.mainSaleProductListPage(pageNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String productId = chatBotRequest.getProductId();
        String userKey = chatBotRequest.getUserKey();

        return productChatBotView.productDetailInfoPage(userKey, productId);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "PATCH/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        return productChatBotView.updateProductStatusResultPage(productId,utterance);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        return productChatBotView.deleteProductResultPage(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "GET/search" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse search(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.ProductListBySearchWordPage(searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/searchNext" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse searchNext(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.ProductListBySearchWordPage(searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.myPurchaseProductListPage(userKey,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProductProfile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();

        return productChatBotView.myPurchaseProductDetailInfoPage(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.mySalesContractProductListPage(userKey,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProduct/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();

        return productChatBotView.mySalesContractProductDetailInfoPage(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/special" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getSpecialProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int currentPage = chatBotRequest.getPageNumber();
        int firstNumber = chatBotRequest.getFirstNumber();

        return productChatBotView.specialProductListPage(currentPage,firstNumber);
    }

}
