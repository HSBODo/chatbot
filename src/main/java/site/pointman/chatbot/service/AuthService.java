package site.pointman.chatbot.service;


import io.jsonwebtoken.Claims;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface AuthService {
    String createSignature (Long timestamp);
    OAuthTokenDto createToken ();


    String createJwtToken(ChatBotRequest chatBotRequest);
    Claims parseClaims(String accessToken);
    boolean isExpired (String accessToken);

    boolean isAuth (ChatBotRequest chatBotRequest);
    ChatBotResponse addJwtToken(ChatBotResponse chatBotResponse, String accessToken);
}

