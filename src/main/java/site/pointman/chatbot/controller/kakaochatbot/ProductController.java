package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.annotation.ValidateMember;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.service.MemberService;
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

        return productChatBotView.verificationCustomerSuccessChatBotResponse();
    }

    @ResponseBody
    @PostMapping(value = "GET/category" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        String requestBlockId = chatBotRequest.getRequestBlockId();

        return productChatBotView.getCategoryChatBotResponse(requestBlockId);
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

        return productChatBotView.getProductInfoPreviewChatBotResponse(imageUrls,category,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl);
    }

    @ResponseBody
    @PostMapping(value = "GET/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.getProductsByCategoryChatBotResponse(category,pageNumber);
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

        return productChatBotView.addProductChatBotResponse(
                productDto,
                userKey,
                imageUrls
                );
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/myProductsByStatus" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productStatus = chatBotRequest.getProductStatus();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.getMyProductsByStatusChatBotResponse(userKey,productStatus,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/mainProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMainProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.getMainProductsChatBotResponse(pageNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String productId = chatBotRequest.getProductId();
        String userKey = chatBotRequest.getUserKey();

        return productChatBotView.getProductChatBotResponse(userKey, productId);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "PATCH/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        return productChatBotView.updateStatusChatBotResponse(productId,utterance);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        return productChatBotView.deleteProductChatBotResponse(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "GET/search" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse search(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.searchProductsChatBotResponse(searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/searchNext" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse searchNext(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.searchProductsChatBotResponse(searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.getPurchaseProducts(userKey,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProductProfile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();

        return productChatBotView.getPurchaseProductProfile(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.getSalesContractProductsChatBotResponse(userKey,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProduct/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();

        return productChatBotView.getSalesContractProductChatBotResponse(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/special" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getSpecialProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int currentPage = chatBotRequest.getPageNumber();
        int firstNumber = chatBotRequest.getFirstNumber();

        return productChatBotView.getSpecialProductsChatBotResponse(currentPage,firstNumber);
    }

}
