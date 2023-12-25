package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.log.ChatBotLog;


@SpringBootTest
@Slf4j
class LogRepositoryTest {

    @Autowired
    LogRepository logRepository;

    @Test
    @Transactional
    void save() {
        //give
        ChatBotLog logEntity = ChatBotLog.builder()
                .userKey("asdasd")
                .build();
        //when
        logRepository.save(logEntity);

        //then
    }
}