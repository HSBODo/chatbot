package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.annotation.SkipLogging;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;
import site.pointman.chatbot.service.chatbot.ValidationChatBotService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/validation")
public class ValidationController {

    ValidationChatBotService validationChatBotService;

    public ValidationController(ValidationChatBotService validationChatBotService) {
        this.validationChatBotService = validationChatBotService;
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "customer/phone" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationPhone(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationCustomerPhoneNumber(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "customer/name" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationName(@RequestBody ChatBotRequest chatBotRequest) {
        return  validationChatBotService.validationCustomerName(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/name" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductName(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationProductName(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/price" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductPrice(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationProductPrice(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/description" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductDescription(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationProductDescription(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/kakaoOpenChatUrl" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductKakaoOpenChatUrl(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationProductKakaoOpenChatUrl(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/tradingLocation" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductTradingLocation(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationTradingLocation(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/reservationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationReservationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationReservationCustomer(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "order/reservationTrackingNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationTrackingNumber(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotService.validationTrackingNumber(chatBotRequest);
    }
}
