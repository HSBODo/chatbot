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
import site.pointman.chatbot.view.kakaochatobotview.ValidationChatBotView;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/validation")
public class ValidationController {

    ValidationChatBotView validationChatBotView;

    public ValidationController(ValidationChatBotView validationChatBotView) {
        this.validationChatBotView = validationChatBotView;
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "customer/phone" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationPhone(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationMemberPhoneNumberResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "customer/name" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationName(@RequestBody ChatBotRequest chatBotRequest) {
        return  validationChatBotView.validationMemberNameResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/name" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductName(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationProductNameResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/price" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductPrice(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationProductPriceResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/description" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductDescription(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationProductDescriptionResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/kakaoOpenChatUrl" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductKakaoOpenChatUrl(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationProductKakaoOpenChatUrlResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/tradingLocation" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationProductTradingLocation(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationTradingLocationResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "product/reservationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationReservationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationReservationMemberResult(chatBotRequest);
    }

    @SkipLogging
    @ResponseBody
    @PostMapping(value = "order/reservationTrackingNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotValidationResponse validationTrackingNumber(@RequestBody ChatBotRequest chatBotRequest) {
        return validationChatBotView.validationTrackingNumberResult(chatBotRequest);
    }
}
