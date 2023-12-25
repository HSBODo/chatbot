package site.pointman.chatbot.service;


import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;

public interface AuthService {
    OAuthTokenDto createNaverOAuthToken();
    String createJwtToken(String name, String userKey);
    boolean isTokenVerification (String token);
    ChatBotResponse addJwtToken(ChatBotResponse chatBotResponse, String accessToken);
}

