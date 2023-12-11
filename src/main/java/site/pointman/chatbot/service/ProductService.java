package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface ProductService {

    ProductListDto getProducts (OAuthTokenDto tokenDto);
    ResponseDto createAddValidation(ChatBotRequest chatBotRequest);

    ResponseDto validationCustomer(ChatBotRequest chatBotRequest);
}
