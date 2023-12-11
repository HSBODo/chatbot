package site.pointman.chatbot.service;


import io.jsonwebtoken.Claims;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface AuthService {
    String createSignature (Long timestamp);
    OAuthTokenDto createToken ();


    String createJwtToken(RequestDto requestDto);
    Claims parseClaims(String accessToken);
    boolean isExpired (String accessToken);

    boolean isAuth (RequestDto requestDto);
    ResponseDto addJwtToken(ResponseDto responseDto, String accessToken);
}

