package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;

public interface ValidationChatBotService {
    ChatBotValidationResponse validationCustomerPhoneNumber(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationCustomerName(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductName(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductPrice(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductDescription(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductKakaoOpenChatUrl(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationReservationCustomer(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationTrackingNumber(ChatBotRequest chatBotRequest);
}
