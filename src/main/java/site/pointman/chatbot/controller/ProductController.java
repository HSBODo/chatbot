package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.ValidationResponseDto;
import site.pointman.chatbot.dto.response.property.common.Extra;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
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
    public ResponseDto auth(@RequestBody RequestDto requestDto) throws Exception {
        ResponseDto response = new ResponseDto();
        try {
            boolean isCustomer = customerService.isCustomer(requestDto);
            if(isCustomer){

                String jwtToken = authService.createJwtToken(requestDto);
                response = authService.addJwtToken(response, jwtToken);

                QuickReplyButtons quickReplyButtons = new QuickReplyButtons();
                quickReplyButtons.addBlockQuickButton("상품등록하기","652a0a9a27e3c4125a33f6eb",null);
                response.addSimpleText("상품등록하러가시겠습니까?");
                response.addQuickButton(quickReplyButtons);
                return response;
            }else {
                response.addException("회원이 아닙니다.");
                return response;
            }
        }catch (Exception e){
            log.info("error = {}", e.getMessage());
            response.addException("시스템 오류");
            return response;
        }
    }

    @ResponseBody
    @PostMapping(value = "priceValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponseDto priceValidation(@RequestBody RequestDto requestDto) throws Exception {
        ValidationResponseDto response = new ValidationResponseDto();
        response.setStatus("SUCCESS");
        return response;
    }

    @ResponseBody
    @PostMapping(value = "nameValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponseDto nameValidation(@RequestBody RequestDto requestDto) throws Exception {
        ValidationResponseDto response = new ValidationResponseDto();
        response.setStatus("SUCCESS");
        return response;
    }

    @ResponseBody
    @PostMapping(value = "descriptionValidation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponseDto descriptionValidation(@RequestBody RequestDto requestDto) throws Exception {
        ValidationResponseDto response = new ValidationResponseDto();
        response.setStatus("SUCCESS");
        return response;
    }

    @ResponseBody
    @PostMapping(value = "addValidation" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto addValidation(@RequestBody RequestDto requestDto) throws Exception {
        ResponseDto response = new ResponseDto();
        try {
            log.info("getAccessToken={}", requestDto.getAccessToken());
            boolean isAuth = authService.isAuth(requestDto);

            if(!isAuth){
                response.addException("처음부터 다시 시작해주세요.");
                return response;
            }

            response = productService.createAddValidation(requestDto);

            return response;
        }catch (Exception e){
            log.info("error = {}", e.getMessage());
            response.addException("시스템 오류");
            return response;
        }
    }

    @ResponseBody
    @PostMapping(value = "add" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto add(@RequestBody RequestDto requestDto) throws Exception {
        ResponseDto response = new ResponseDto();
        try {




            return response;
        }catch (Exception e){
            log.info("error = {}", e.getMessage());
            response.addException("시스템 오류");
            return response;
        }
    }
}
