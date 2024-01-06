package site.pointman.chatbot.globalservice;


import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;

public interface AuthService {
    String createJwtToken(String name, String userKey);
    boolean isTokenVerification (String token);
    ChatBotResponse addJwtToken(ChatBotResponse chatBotResponse, String accessToken);
}

