package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.log.ChatBotLog;

public interface LogRepository {
    void save(ChatBotLog log);
}
