package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.log.ChatBotLog;
import site.pointman.chatbot.domain.request.ChatBotRequest;

public interface LogService {
    ChatBotLog insertChatBotRequestLog(ChatBotRequest chatBotRequest) throws Exception;
    void insertChatBotResponseLog(ChatBotLog log, Object chatBotResponse);
}
