package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.log.Log;


@SpringBootTest
@Slf4j
class LogRepositoryTest {

    @Autowired
    LogRepository logRepository;

    @Test
    @Transactional
    void save() {
        Log logEntity = Log.builder()
                .userKey("asdasd")
                .build();
        logRepository.save(logEntity);
    }
}