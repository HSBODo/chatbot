package site.pointman.chatbot.service;


import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;

public interface AuthService {
    String createSignature (Long timestamp);

    OAuthTokenDto createToken ();
}

