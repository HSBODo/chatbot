package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.vo.KakaoResponseVo;

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
    void createLocationNotice() throws ParseException {
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        String kakaoUserkey = "21321312";
        kakaoResponseVo.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
        JSONObject result = kakaoResponseVo.createKakaoResponse();
        log.info("createLocationNotice={},",result);
    }
    @Test
    void todayWeather() throws Exception {
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        String kakaoUserkey= "QFERwysZbO77";

        kakaoResponseVo.addContent( kakaoApiService.createTodayWeather(kakaoUserkey));
        JSONObject result = kakaoResponseVo.createKakaoResponse();
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
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        kakaoResponseVo.addContent(recommendItems);
        log.info("createRecommendItems={}", kakaoResponseVo.createKakaoResponse());

    }

    @Test
    void createTodayNews() throws Exception {
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        kakaoResponseVo.addContent(kakaoApiService.createTodayNews("뉴스"));
        log.info("createTodayNews={}",  kakaoResponseVo.createKakaoResponse()       );
    }
}