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
    public ChatBotResponse join(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.join(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "get/info" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse info(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.getCustomerInfo(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "update/PhoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.updateCustomerPhoneNumber(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "delete" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.deleteCustomer(chatBotRequest);
    }

}
