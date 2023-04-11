package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.wearher.WeatherElementCode;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class WeatherApiServiceTest {
    @Autowired
    WeatherApiService weatherApiService;
    @Test
    void selectShortTermWeather() {
        KakaoMemberLocation kakaoMemberLocation= new KakaoMemberLocation();
        kakaoMemberLocation.setX(BigDecimal.valueOf(37.4603776));
        kakaoMemberLocation.setY(BigDecimal.valueOf(126.8187136));
        WeatherElementCode result = weatherApiService.selectShortTermWeather(kakaoMemberLocation);
        log.info("result = {}",result.getBaseDate());
    }

    @Test
    void weatherCodeFindByName() {
    }
}