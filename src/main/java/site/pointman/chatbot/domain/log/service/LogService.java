package site.pointman.chatbot.domain.log.service;

import site.pointman.chatbot.domain.log.ChatBotLog;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;

public interface LogService {
    ChatBotLog insertChatBotRequestLog(ChatBotRequest chatBotRequest) throws Exception;
    void insertChatBotResponseLog(ChatBotLog log, Object chatBotResponse);
}
