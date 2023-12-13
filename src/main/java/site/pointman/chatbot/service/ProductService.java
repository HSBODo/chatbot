package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface ProductService {


    ChatBotResponse createProductInfoPreview(ChatBotRequest chatBotRequest);
    ChatBotResponse addProduct(ChatBotRequest chatBotRequest);
    ChatBotResponse validationCustomer(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductName(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductPrice(ChatBotRequest chatBotRequest);
    ValidationResponse validationProductDescription(ChatBotRequest chatBotRequest);
    ValidationResponse validationKakaoOpenChatUrl(ChatBotRequest chatBotRequest);

    ValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest);
}
