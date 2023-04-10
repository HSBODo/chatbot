package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.BasicCard;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.KakaoResponse;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class KakaoApiServiceTest {
    @Autowired
    private KakaoApiService kakaoApiService;

    @Test
    void createBasicCard() {
    }

    @Test
    void createSimpleText() {
    }

    @Test
    void createSimpleImage() {
    }

    @Test
    void createCommerceCard() {
    }

    @Test
    void createListCard() {
    }


    @Test
    void createLocationNotice() throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        BasicCard basicCard = new BasicCard();
        String kakaoUserkey = "21321312";
        kakaoResponse.addContent(kakaoApiService.createLocationNotice(basicCard,kakaoUserkey));
        JSONObject result = kakaoResponse.createKakaoResponse();
        log.info("result={},",result);
    }
    @Test
    void todayWeather() throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        KakaoMemberLocation kakaoMemberLocation = new KakaoMemberLocation();
        kakaoMemberLocation.setX("37.4603776");
        kakaoMemberLocation.setY("126.8187136");
        kakaoMemberLocation.setKakaoUserkey("asdasdasd");

        kakaoResponse.addContent( kakaoApiService.createTodayWeather(kakaoMemberLocation));
        JSONObject result = kakaoResponse.createKakaoResponse();
        log.info("result={},",result);

    }
}