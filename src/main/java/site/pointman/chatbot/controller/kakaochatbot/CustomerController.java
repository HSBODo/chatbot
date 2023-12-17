package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.service.CustomerService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/join" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse join(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.join(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse profile(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.getCustomerProfile(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "PATCH/PhoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.updateCustomerPhoneNumber(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.deleteCustomer(chatBotRequest);
    }

}
