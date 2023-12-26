package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.chatbot.ProductChatBotResponseService;
import site.pointman.chatbot.utill.CustomNumberUtils;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {


    ProductChatBotResponseService productChatBotResponseService;
    MemberService memberService;
    OrderService orderService;
    ProductService productService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public ProductController(ProductChatBotResponseService productChatBotResponseService, MemberService memberService, OrderService orderService, ProductService productService) {
        this.productChatBotResponseService = productChatBotResponseService;
        this.memberService = memberService;
        this.orderService = orderService;
        this.productService = productService;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public Object verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productChatBotResponseService.verificationCustomerSuccessChatBotResponse();
    }

    @ResponseBody
    @PostMapping(value = "GET/category" , headers = {"Accept=application/json; UTF-8"})
    public Object getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        String requestBlockId = chatBotRequest.getRequestBlockId();

        return productChatBotResponseService.getCategoryChatBotResponse(requestBlockId);
    }

    @ResponseBody
    @PostMapping(value = "GET/preview" , headers = {"Accept=application/json; UTF-8"})
    public Object preview(@RequestBody ChatBotRequest chatBotRequest) {
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productName = chatBotRequest.getProductName();
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
        String category = chatBotRequest.getChoiceParam();

        return productChatBotResponseService.getProductInfoPreviewSuccessChatBotResponse(imageUrls,category,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl);
    }

    @ResponseBody
    @PostMapping(value = "GET/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public Object getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());

        return productChatBotResponseService.createProductListChatBotResponse(category);
    }

    @ResponseBody
    @PostMapping(value = "POST" , headers = {"Accept=application/json; UTF-8"})
    public Object add(@RequestBody ChatBotRequest chatBotRequest){
        String userKey = chatBotRequest.getUserKey();
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productCategory = chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue();
        ProductDto productDto = chatBotRequest.createProductDto();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        Long productId = CustomNumberUtils.createNumberId();

        return productChatBotResponseService.addProductSuccessChatBotResponse(
                productDto,
                productId,
                userKey,
                imageUrls,
                productCategory
                );
    }

    @ResponseBody
    @PostMapping(value = "GET/myProductsByStatus" , headers = {"Accept=application/json; UTF-8"})
    public Object getMyProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productStatus = chatBotRequest.getProductStatus();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productChatBotResponseService.createMyProductListChatBotResponse(userKey,productStatus);
    }

    @ResponseBody
    @PostMapping(value = "GET/mainProducts" , headers = {"Accept=application/json; UTF-8"})
    public Object getMainProducts(@RequestBody ChatBotRequest chatBotRequest) {
        return productChatBotResponseService.getMainProductsChatBotResponse();
    }

    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public Object getProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String productId = chatBotRequest.getProductId();
        String userKey = chatBotRequest.getUserKey();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productChatBotResponseService.getProductProfileSuccessChatBotResponse(userKey, productId);
    }

    @ResponseBody
    @PostMapping(value = "PATCH/status" , headers = {"Accept=application/json; UTF-8"})
    public Object updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productChatBotResponseService.updateStatusSuccessChatBotResponse(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public Object delete(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productChatBotResponseService.deleteProductSuccessChatBotResponse(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "GET/search" , headers = {"Accept=application/json; UTF-8"})
    public Object search(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();

        return productChatBotResponseService.createProductListChatBotResponse(searchWord);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProducts" , headers = {"Accept=application/json; UTF-8"})
    public Object getPurchaseProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return orderService.getPurchaseProducts(userKey);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProductProfile" , headers = {"Accept=application/json; UTF-8"})
    public Object getPurchaseProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();

        return orderService.getPurchaseProductProfile(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProducts" , headers = {"Accept=application/json; UTF-8"})
    public Object getContractProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return productChatBotResponseService.getContractProductsSuccessChatBotResponse(userKey);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProduct/profile" , headers = {"Accept=application/json; UTF-8"})
    public Object getContractProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String orderId = chatBotRequest.getOrderId();
        return productChatBotResponseService.getContractProductProfileSuccessChatBotResponse(userKey,orderId);
    }

    @ResponseBody
    @PostMapping(value = "GET/special" , headers = {"Accept=application/json; UTF-8"})
    public Object getSpecialProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int currentPage = chatBotRequest.getPageNumber();
        int firstNumber = chatBotRequest.getFirstNumber();

        return productChatBotResponseService.getSpecialProductsSuccessChatBotResponse(currentPage,firstNumber);
    }

}
