package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.request.RequestDto;

public interface LoginService {

    String createJWTToken(RequestDto requestDto);
    String getDataFromJWTToken(String token);
}
