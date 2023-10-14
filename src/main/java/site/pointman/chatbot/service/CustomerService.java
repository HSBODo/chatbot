package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface CustomerService {
    ResponseDto join(RequestDto requestDto);
}
