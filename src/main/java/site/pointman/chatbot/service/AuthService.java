package site.pointman.chatbot.service;


import io.jsonwebtoken.Claims;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface AuthService {
    String createSignature (Long timestamp);
    OAuthTokenDto createToken ();


    String createJwtToken(ChatBotRequest chatBotRequest);
    Claims parseClaims(String accessToken);
    boolean isExpired (String accessToken);

    boolean isAuth (ChatBotRequest chatBotRequest);
    ResponseDto addJwtToken(ResponseDto responseDto, String accessToken);
}

