package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.dto.response.ResponseDto;
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
    public ResponseDto auth(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ResponseDto response = productService.validationCustomer(chatBotRequest);
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
    public ResponseDto preview(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ResponseDto response = productService.createProductInfoPreview(chatBotRequest);
        return response;
    }



    @ResponseBody
    @PostMapping(value = "add" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto add(@RequestBody String chatBotRequest) throws Exception {
        log.info("request={}",chatBotRequest);
        return null;
    }
}
