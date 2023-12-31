package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotValidationResponse;

public interface ValidationChatBotView {
    ChatBotValidationResponse validationMemberPhoneNumberResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationMemberNameResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductNameResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductPriceResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductDescriptionResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationProductKakaoOpenChatUrlResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationTradingLocationResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationReservationMemberResult(ChatBotRequest chatBotRequest);
    ChatBotValidationResponse validationTrackingNumberResult(ChatBotRequest chatBotRequest);
}
