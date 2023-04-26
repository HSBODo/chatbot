package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReadyDto;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCodeDto;
import site.pointman.chatbot.repository.ItemRepository;

import java.math.BigDecimal;

@Slf4j
@SpringBootTest
class OpenApiServiceTest {
    @Autowired
    OpenApiService openApiService;
    @Test
    void selectShortTermWeather() {
        KakaoMemberLocation kakaoMemberLocation= KakaoMemberLocation.builder()
                .x(BigDecimal.valueOf(37.4603776))
                .y(BigDecimal.valueOf(126.8187136))
                .build();
        WeatherPropertyCodeDto result = openApiService.selectShortTermWeather(kakaoMemberLocation);
   }
   @Test
    void selectNaverSearch() {
       SearchDto searchDto = openApiService.selectNaverSearch("뉴스", "5", "1", "date");
   }

    @Test
    void kakaoPayReady() throws Exception {
       openApiService.kakaoPayReady(2L,"QFERwysZbO77");
    }
    @Test
    void kakaoPayApprove() throws Exception {
        openApiService.kakaoPayApprove("ff5c5b1a30fe5965f1beee6c459d67b365cf788666077404f12139cc74198ced",322448282L);
    }
    @Test
    void kakaoPayCancel() throws Exception {
        Long cancelOrderId=170091396L;
        openApiService.kakaoPayCancel(cancelOrderId);
    }


}