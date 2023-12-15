package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.log.Log;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface LogService {
    Log insert(ChatBotRequest chatBotRequest) throws Exception;
    void insert(Log log, ChatBotResponse chatBotResponse);
}
