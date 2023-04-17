package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCode;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class OpenApiServiceTest {
    @Autowired
    OpenApiService openApiService;
    @Test
    void selectShortTermWeather() {
        KakaoMemberLocation kakaoMemberLocation= new KakaoMemberLocation();
        kakaoMemberLocation.setX(BigDecimal.valueOf(37.4603776));
        kakaoMemberLocation.setY(BigDecimal.valueOf(126.8187136));
        WeatherPropertyCode result = openApiService.selectShortTermWeather(kakaoMemberLocation);
        log.info("result = {}",result.toString());
        assertInstanceOf(WeatherPropertyCode.class,result);
   }
   @Test
    void selectNaverSearch() throws ParseException {
       openApiService.selectNaverSearch("뉴스","5","1","date");
    }

    @Test
    void kakaoPayReady() throws Exception {
        //openApiService.kakaoPayReady();
    }


}