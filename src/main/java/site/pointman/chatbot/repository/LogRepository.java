package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.log.ChatBotLog;

public interface LogRepository extends JpaRepository<ChatBotLog,Long> {

}
