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
@RequestMapping(value = "/kakaochatbot/")
public class ProductController {

    ProductService productService;
    LogService logService;

    public ProductController(ProductService productService, LogService logService) {
        this.productService = productService;
        this.logService = logService;
    }

    @ResponseBody
    @PostMapping(value = "customer/add/product/auth" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse auth(@RequestBody ChatBotRequest chatBotRequest) {
        ChatBotResponse response = productService.validationCustomer(chatBotRequest);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "product/preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getProductInfoPreview(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/add" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest){
        return productService.addProduct(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/get/all" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProducts(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getCustomerProducts(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/get/detail" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductDetail(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.getCustomerProductDetail(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/update/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.updateProductStatus(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/delete" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        return productService.deleteProduct(chatBotRequest);
    }

}
