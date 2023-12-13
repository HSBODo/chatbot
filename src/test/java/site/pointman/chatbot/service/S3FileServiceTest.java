package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class S3FileServiceTest {
    @Autowired
    S3FileService s3FileService;

    @Test
    void upload() {
        File file = new File("src/main/resources/image/온도3");
        String url = s3FileService.upload(file, "image");
        log.info("url={}",url);
    }
}