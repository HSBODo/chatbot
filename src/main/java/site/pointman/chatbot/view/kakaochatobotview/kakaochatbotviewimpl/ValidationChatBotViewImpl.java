package site.pointman.chatbot.view.kakaochatobotview.kakaochatbotviewimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotValidationResponse;
import site.pointman.chatbot.globalservice.ValidationService;
import site.pointman.chatbot.view.kakaochatobotview.ValidationChatBotView;

@Service
public class ValidationChatBotViewImpl implements ValidationChatBotView {
    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    ValidationService validationService;

    public ValidationChatBotViewImpl(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public ChatBotValidationResponse validationMemberPhoneNumberResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputPhone = chatBotRequest.getValidationData();

        if(validationService.isValidMemberPhoneNumber(inputPhone)){

            inputPhone = inputPhone.replaceAll("-", "");
            chatBotValidationResponse.validationSuccess(inputPhone);
            return chatBotValidationResponse;

        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationMemberNameResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputName = chatBotRequest.getValidationData();

        if(validationService.isValidMemberName(inputName)){

            chatBotValidationResponse.validationSuccess(inputName);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductNameResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productName = chatBotRequest.getValidationData();

        if(validationService.isValidProductName(productName)){

            chatBotValidationResponse.validationSuccess(productName);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductPriceResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productPrice= chatBotRequest.getValidationData();

        if(validationService.isValidProductPrice(productPrice)){
            chatBotValidationResponse.validationSuccess(productPrice);
            return chatBotValidationResponse;

        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductDescriptionResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productDescription= chatBotRequest.getValidationData();

        if(validationService.isValidProductDescription(productDescription)){

            chatBotValidationResponse.validationSuccess(productDescription);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductKakaoOpenChatUrlResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String kakaoOpenChayUrl= chatBotRequest.getValidationData();

        if(validationService.isValidKakaoOpenChatUrl(kakaoOpenChayUrl)){

            chatBotValidationResponse.validationSuccess(kakaoOpenChayUrl);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationTradingLocationResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String tradingLocation= chatBotRequest.getValidationData();
        chatBotValidationResponse.validationSuccess(tradingLocation);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationReservationMemberResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String reservationCustomerName= chatBotRequest.getValidationData();


        if(validationService.isValidReservationMember(reservationCustomerName)){
            chatBotValidationResponse.validationSuccess(reservationCustomerName);
            return chatBotValidationResponse;


        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationTrackingNumberResult(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String trackingNumber = chatBotRequest.getValidationData();

        if(validationService.isValidTrackingNumber(trackingNumber)){

            chatBotValidationResponse.validationSuccess(trackingNumber);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }
}
