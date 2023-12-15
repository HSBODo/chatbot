package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.log.Log;

public interface LogRepository {
    void save(Log log);
}
