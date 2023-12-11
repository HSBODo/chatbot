package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface ProductService {
    ProductListDto getProducts (OAuthTokenDto tokenDto);

    ResponseDto createAddValidation(RequestDto requestDto);
}
