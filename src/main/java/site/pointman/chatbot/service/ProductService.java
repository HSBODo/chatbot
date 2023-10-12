package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;

public interface ProductService {
    ProductListDto getProducts (OAuthTokenDto tokenDto);

}
