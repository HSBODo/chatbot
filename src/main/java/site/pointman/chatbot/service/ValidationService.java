package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;

public interface ValidationService {
    ChatBotValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationCustomerName(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductName(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductPrice(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductDescription(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationReservationCustomer(ChatBotRequest chatBotRequest);
}
