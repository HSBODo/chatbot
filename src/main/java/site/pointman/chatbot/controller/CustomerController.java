package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.service.CustomerService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ResponseBody
    @PostMapping(value = "join" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto join(@RequestBody String requestDto) throws Exception {
        ResponseDto response = new ResponseDto();
        log.info("req ={}", requestDto);
        try {
            //response = customerService.join(requestDto);
            return response;
        }catch (Exception e){
            log.info("error = {}", e.getMessage());
            response.addSimpleText("시스템 오류");
            return response;
        }
    }

}
