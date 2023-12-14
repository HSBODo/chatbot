package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ValidationResponse;

public interface ValidationService {
    ValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest);
    ValidationResponse validationCustomerName(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductName(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductPrice(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductDescription(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest);
    ValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest);

}
