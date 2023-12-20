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
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.utill.NumberUtils;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {


    ProductService productService;
    MemberService memberService;
    ChatBotExceptionResponse chatBotExceptionResponse;

    public ProductController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public Response verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.verificationCustomerSuccessResponse();
    }

    @ResponseBody
    @PostMapping(value = "GET/category" , headers = {"Accept=application/json; UTF-8"})
    public Response getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        String requestBlockId = chatBotRequest.getRequestBlockId();

        return productService.getProductCategory(requestBlockId);
    }

    @ResponseBody
    @PostMapping(value = "GET/preview" , headers = {"Accept=application/json; UTF-8"})
    public Response preview(@RequestBody ChatBotRequest chatBotRequest) {
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productName = chatBotRequest.getProductName();
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
        String category = chatBotRequest.getChoiceParam();

        return productService.getProductInfoPreview(imageUrls,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl,category);
    }

    @ResponseBody
    @PostMapping(value = "GET/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public Response getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());

        return productService.getProductsByCategory(category);
    }

    @ResponseBody
    @PostMapping(value = "POST" , headers = {"Accept=application/json; UTF-8"})
    public Response add(@RequestBody ChatBotRequest chatBotRequest){
        String userKey = chatBotRequest.getUserKey();
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productCategory = chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue();
        ProductDto productDto = chatBotRequest.createProductDto();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        Long productId = NumberUtils.createNumberId();

        return productService.addProduct(
                productDto,
                productId,
                userKey,
                imageUrls,
                productCategory
                );
    }

    @ResponseBody
    @PostMapping(value = "GET/byUserKey" , headers = {"Accept=application/json; UTF-8"})
    public Response getProductsByUserKey(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productStatus = chatBotRequest.getProductStatus();
        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.getProductsByUserKey(userKey,productStatus);
    }

    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public Response getProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String productId = chatBotRequest.getProductId();
        String userKey = chatBotRequest.getUserKey();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.getProductProfile(productId, userKey);
    }

    @ResponseBody
    @PostMapping(value = "PATCH/status" , headers = {"Accept=application/json; UTF-8"})
    public Response updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.updateProductStatus(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public Response delete(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String productId = chatBotRequest.getProductId();
        String utterance = chatBotRequest.getUtterance();

        if(!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.deleteProduct(productId,utterance);
    }

    @ResponseBody
    @PostMapping(value = "GET/search" , headers = {"Accept=application/json; UTF-8"})
    public Response search(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();

        return productService.getProductsBySearchWord(searchWord);
    }

}
