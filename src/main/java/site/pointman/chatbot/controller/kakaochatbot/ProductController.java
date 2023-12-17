package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.service.LogService;
import site.pointman.chatbot.service.ProductService;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/product")
public class ProductController {

    ProductService productService;
    LogService logService;

    public ProductController(ProductService productService, LogService logService) {
        this.productService = productService;
        this.logService = logService;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 자원(Resource)/행위(HTTP Method) 로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "post/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        ChatBotResponse response = productService.verificationCustomer(chatBotRequest);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "get/category" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductCategory(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "get/all/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductsByCategory(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductInfoPreview(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "add" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest){
        return productService.addProduct(chatBotRequest);
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
