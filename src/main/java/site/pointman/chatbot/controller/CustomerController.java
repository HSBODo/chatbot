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
    public ResponseDto join(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.join(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "isPhone" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse isPhone(@RequestBody ChatBotRequest chatBotRequest) {
        ValidationResponse validationResultResponse = customerService.validationFormatPhoneNumber(chatBotRequest);
        return validationResultResponse;
    }

    @ResponseBody
    @PostMapping(value = "info" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto info(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.getCustomerInfo(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "update/PhoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.updateCustomerPhoneNumber(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "delete" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto delete(@RequestBody ChatBotRequest chatBotRequest) {
        return customerService.deleteCustomer(chatBotRequest);
    }

}