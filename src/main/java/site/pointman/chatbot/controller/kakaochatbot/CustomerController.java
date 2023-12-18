package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.CustomerService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class CustomerController {

    CustomerService customerService;
    ChatBotExceptionResponse chatBotExceptionResponse;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/join" , headers = {"Accept=application/json; UTF-8"})
    public Response join(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String name = chatBotRequest.getCustomerName();
        String phoneNumber = chatBotRequest.getCustomerPhone();

        if (customerService.isCustomer(userKey)) return chatBotExceptionResponse.createException("이미 존재하는 회원입니다.");

        return customerService.join(userKey, name, phoneNumber,true);
    }

    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public Response getProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!customerService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerService.getCustomerProfile(userKey, true);
    }

    @ResponseBody
    @PostMapping(value = "PATCH/PhoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public Response updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String updatePhoneNumber = chatBotRequest.getCustomerPhone();

        if (!customerService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerService.updateCustomerPhoneNumber(userKey, updatePhoneNumber,true);
    }

    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public Response withdrawalCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!customerService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerService.withdrawalCustomer(userKey, true);
    }
}
