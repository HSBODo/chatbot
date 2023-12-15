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

    @ResponseBody
    @PostMapping(value = "auth" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse auth(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ChatBotResponse response = productService.validationCustomer(chatBotRequest);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return productService.getProductInfoPreview(chatBotRequest);
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
