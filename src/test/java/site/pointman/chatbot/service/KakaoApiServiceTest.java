package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.KakaoResponseDto;

@Slf4j
@SpringBootTest
class KakaoApiServiceTest {
    @Autowired
    private KakaoApiService kakaoApiService ;

    @Test
    void createOrderList() throws Exception {
        String kakaoUserkey= "QFERwysZbO77";
        JSONObject orderList = kakaoApiService.createOrderList(kakaoUserkey);
        log.info("createOrderList={}",orderList);
    }

    @Test
    void createDeveloperInfo() throws ParseException {
        JSONObject result = kakaoApiService.createDeveloperInfo();
        log.info("createDeveloperInfo={}",result);
    }

    @Test
    void createLocationNotice() throws Exception {
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        String kakaoUserkey = "21321312";
        kakaoResponseDto.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
        JSONObject result = kakaoResponseDto.createKakaoResponse();
        log.info("createLocationNotice={},",result);
    }
    @Test
    void todayWeather() throws Exception {
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        String kakaoUserkey= "QFERwysZbO77";

        kakaoResponseDto.addContent( kakaoApiService.createTodayWeather(kakaoUserkey));
        JSONObject result = kakaoResponseDto.createKakaoResponse();
        log.info("todayWeather={},",result);

    }
    @Test
    void createOrderDetail() throws Exception {
        Long orderId= 189457795L;
        String kakaoUserkey= "QFERwysZbO77";
        JSONObject orderDetail = kakaoApiService.createOrderDetail(kakaoUserkey, orderId);
        log.info("createOrderDetail={}",orderDetail);
    }

    @Test
    void createRecommendItems() throws Exception {
        String kakaoUserkey= "QFERwysZbO77";
        JSONObject recommendItems = kakaoApiService.createRecommendItems(kakaoUserkey);
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        kakaoResponseDto.addContent(recommendItems);
        log.info("createRecommendItems={}", kakaoResponseDto.createKakaoResponse());

    }

    @Test
    void createTodayNews() throws Exception {
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        kakaoResponseDto.addContent(kakaoApiService.createTodayNews("뉴스"));
        log.info("createTodayNews={}",  kakaoResponseDto.createKakaoResponse()       );
    }
}