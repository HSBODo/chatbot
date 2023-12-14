package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.service.ValidationService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/validation")
public class ValidationController {

    ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @ResponseBody
    @PostMapping(value = "customer/phone" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationPhone(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationCustomerPhoneNumber(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "customer/name" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationName(@RequestBody ChatBotRequest chatBotRequest) {
        return  validationService.validationCustomerName(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/name" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationProductName(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationProductName(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/price" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationProductPrice(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationProductPrice(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/description" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationProductDescription(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationProductDescription(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/kakaoOpenChatUrl" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationProductKakaoOpenChatUrl(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationProductKakaoOpenChatUrl(chatBotRequest);
    }

    @ResponseBody
    @PostMapping(value = "product/tradingLocation" , headers = {"Accept=application/json; UTF-8"})
    public ValidationResponse validationProductTradingLocation(@RequestBody ChatBotRequest chatBotRequest) {
        return validationService.validationTradingLocation(chatBotRequest);
    }
}
