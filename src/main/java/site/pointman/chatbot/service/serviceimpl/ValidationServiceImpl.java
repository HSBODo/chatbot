package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.service.ValidationService;
import site.pointman.chatbot.utill.NumberUtils;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    @Override
    public ValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String inputPhone = chatBotRequest.getValidationData();
        inputPhone = inputPhone.replaceAll("-", "");

        if(!NumberUtils.isNumber(inputPhone)){
            validationResponse.validationFail();
            return validationResponse;
        }

        if (inputPhone.length() != 11) {
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(inputPhone);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationCustomerName(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String inputName = chatBotRequest.getValidationData();
        validationResponse.validationSuccess(inputName);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductName(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productName = chatBotRequest.getValidationData();
        if(productName.length()>30){
            validationResponse.validationFail();
            return validationResponse;
        }
        validationResponse.validationSuccess(productName);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductPrice(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productPrice= chatBotRequest.getValidationData();

        if(!NumberUtils.isNumber(productPrice)){
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(productPrice);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductDescription(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productDescription= chatBotRequest.getValidationData();

        if(productDescription.length()>400){
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(productDescription);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String kakaoOpenChayUrl= chatBotRequest.getValidationData();

        if(kakaoOpenChayUrl.contains(KAKAO_OPEN_CHAT_URL_REQUIRED)){
            validationResponse.validationSuccess(kakaoOpenChayUrl);
            return validationResponse;
        }

        validationResponse.validationFail();
        return validationResponse;
    }

    @Override
    public ValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String tradingLocation= chatBotRequest.getValidationData();
        validationResponse.validationSuccess(tradingLocation);
        return validationResponse;
    }

}
