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
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.utill.BlockId;

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
        ResponseDto response = new ResponseDto();
        response = productService.validationCustomer(chatBotRequest);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "priceValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse priceValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ValidationResponse response = new ValidationResponse();

        return response;
    }

    @ResponseBody
    @PostMapping(value = "nameValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse nameValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ValidationResponse response = new ValidationResponse();

        return response;
    }

    @ResponseBody
    @PostMapping(value = "descriptionValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse descriptionValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        ValidationResponse response = new ValidationResponse();

        return response;
    }

    @ResponseBody
    @PostMapping(value = "addValidation" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto addValidation(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
//        ResponseDto response = new ResponseDto();
//        try {
//            log.info("getAccessToken={}", chatBotRequest.getAccessToken());
//            boolean isAuth = authService.isAuth(chatBotRequest);
//
//            if(!isAuth){
//                response.addException("처음부터 다시 시작해주세요.");
//                return response;
//            }
//
//            response = productService.createAddValidation(chatBotRequest);
//
//            return response;
//        }catch (Exception e){
//            log.info("error = {}", e.getMessage());
//            response.addException("시스템 오류");
//            return response;
//        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "add" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto add(@RequestBody ChatBotRequest chatBotRequest) throws Exception {
        return null;
    }
}
