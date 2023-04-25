package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReadyDto;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCodeDto;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

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
        WeatherPropertyCodeDto result = openApiService.selectShortTermWeather(kakaoMemberLocation);
        log.info("result = {}",result.toString());
        assertInstanceOf(WeatherPropertyCodeDto.class,result);
   }
   @Test
    void selectNaverSearch() throws ParseException {
       openApiService.selectNaverSearch("뉴스","5","1","date");
    }

    @Test
    void kakaoPayReady() throws Exception {
        KakaoPayReadyDto kakaoPayReadyDto = KakaoPayReadyDto.builder()
                .kakaoUserkey("QFERwysZbO77")
                .cid("TC0ONETIME")
                .cancel_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .approval_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .item_name("test")
                .partner_order_id("partner_order_id")
                .fail_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .total_amount(3000)
                .partner_user_id("partner_user_id")
                .item_code(1L)
                .quantity(1)
                .tax_free_amount(0)
                .vat_amount(0).build();


     openApiService.kakaoPayReady(kakaoPayReadyDto);
    }
    @Test
    void kakaoPayApprove() throws Exception {
        KakaoPayReadyDto kakaoPayReadyDto = KakaoPayReadyDto.builder()
                .kakaoUserkey("QFERwysZbO77")
                .cid("TC0ONETIME")
                .cancel_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .approval_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .item_name("test")
                .partner_order_id("partner_order_id")
                .fail_url("https://www.pointman.shop/kakaochat/v1/kakaopay-approve")
                .total_amount(3000)
                .partner_user_id("partner_user_id")
                .item_code(1L)
                .quantity(1)
                .tax_free_amount(0)
                .vat_amount(0).build();
        openApiService.kakaoPayApprove("ff5c5b1a30fe5965f1beee6c459d67b365cf788666077404f12139cc74198ced",322448282L);
    }

    @Test
    void kakaoPayCancel() throws Exception {
        openApiService.kakaoPayCancel(170091396L);
    }


}