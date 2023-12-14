package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.service.ProductService;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {

    CustomerService customerService;
    AuthService authService;
    ProductService productService;

    public ProductController(CustomerService customerService, AuthService authService, ProductService productService) {
        this.customerService = customerService;
        this.authService = authService;
        this.productService = productService;
    }

    @ResponseBody
    @PostMapping(value = "auth" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse auth(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ChatBotResponse response = productService.validationCustomer(chatBotRequest);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "priceValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse priceValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.validationProductPrice(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "nameValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse nameValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.validationProductName(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "descriptionValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse descriptionValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.validationProductDescription(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "kakaoOpenChatUrlValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse kakaoOpenChatUrlValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.validationKakaoOpenChatUrl(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "tradingLocationValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse tradingLocationValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.validationTradingLocation(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.createProductInfoPreview(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "add" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.addProduct(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "get" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProducts(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.getCustomerProducts(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "get/detail" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductDetail(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.getCustomerProductDetail(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "update/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.updateProductStatus(chatBotRequest);
    }
    @ResponseBody
    @PostMapping(value = "delete" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.deleteProduct(chatBotRequest);
    }

}
