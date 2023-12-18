package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;
import site.pointman.chatbot.service.ValidationService;
import site.pointman.chatbot.utill.NumberUtils;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    @Override
    public ChatBotValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputPhone = chatBotRequest.getValidationData();
        inputPhone = inputPhone.replaceAll("-", "");

        if(!NumberUtils.isNumber(inputPhone)){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        if (inputPhone.length() != 11) {
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(inputPhone);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationCustomerName(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String inputName = chatBotRequest.getValidationData();
        chatBotValidationResponse.validationSuccess(inputName);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductName(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productName = chatBotRequest.getValidationData();
        if(productName.length()>30){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }
        chatBotValidationResponse.validationSuccess(productName);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductPrice(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productPrice= chatBotRequest.getValidationData();

        if(!NumberUtils.isNumber(productPrice)){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(productPrice);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductDescription(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String productDescription= chatBotRequest.getValidationData();

        if(productDescription.length()>400){
            chatBotValidationResponse.validationFail();
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationSuccess(productDescription);
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String kakaoOpenChayUrl= chatBotRequest.getValidationData();

        if(kakaoOpenChayUrl.contains(KAKAO_OPEN_CHAT_URL_REQUIRED)){
            chatBotValidationResponse.validationSuccess(kakaoOpenChayUrl);
            return chatBotValidationResponse;
        }

        chatBotValidationResponse.validationFail();
        return chatBotValidationResponse;
    }

    @Override
    public ChatBotValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest) {
        ChatBotValidationResponse chatBotValidationResponse = new ChatBotValidationResponse();
        String tradingLocation= chatBotRequest.getValidationData();
        chatBotValidationResponse.validationSuccess(tradingLocation);
        return chatBotValidationResponse;
    }

}
