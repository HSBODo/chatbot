package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.utill.NumberUtils;
import site.pointman.chatbot.utill.StringUtils;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {


    ProductService productService;
    CustomerService customerService;
    ChatBotExceptionResponse chatBotExceptionResponse;

    public ProductController(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 자원(Resource)/행위(HTTP Method) 로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "post/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        final String userKey = chatBotRequest.getUserKey();

        if (!customerService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.verificationCustomerSuccessResponse();
    }

    @ResponseBody
    @PostMapping(value = "get/category" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        final String requestBlockId = chatBotRequest.getRequestBlockId();

        return productService.getProductCategory(requestBlockId);
    }

    @ResponseBody
    @PostMapping(value = "get/preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) {
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productName = chatBotRequest.getProductName();
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = StringUtils.formatPrice(Integer.parseInt(chatBotRequest.getProductPrice()));
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
        String category = chatBotRequest.getChoiceParam();

        return productService.getProductInfoPreview(imageUrls,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl,category);
    }

    @ResponseBody
    @PostMapping(value = "get/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());

        return productService.getProductsByCategory(category);
    }

    @ResponseBody
    @PostMapping(value = "post" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest){
        final Long productId = NumberUtils.createProductId();
        final String userKey = chatBotRequest.getUserKey();
        final List<String> imageUrls = chatBotRequest.getProductImages();
        final Category productCategory = Category.getCategory(chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue());
        final ProductDto productDto = chatBotRequest.createProductDto();

        if(!customerService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return productService.addProduct(
                productDto,
                productId,
                userKey,
                imageUrls,
                productCategory
                );
    }

    @ResponseBody
    @PostMapping(value = "get/all/byUserKey" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByUserKey(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductsByUserKey(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "get" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProduct(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductProfile(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "update/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.updateProductStatus(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "delete" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.deleteProduct(chatBotRequest);
    }

}
