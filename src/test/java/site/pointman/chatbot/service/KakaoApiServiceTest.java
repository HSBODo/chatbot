package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.kakaoui.KakaoResponse;
import site.pointman.chatbot.dto.kakaoui.Button;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class KakaoApiServiceTest {



    @Autowired
    private KakaoApiService kakaoApiService ;





    @Test
    void createBasicCard() throws ParseException {
        List<Button> buttons = new ArrayList();
        Button button = new Button("action","laber","www.asdasd.com");
        Button button2 = new Button("action1","label1","www.asdasd.com1");
        buttons.add(button);
        buttons.add(button2);
        JSONObject result = kakaoApiService.createBasicCard("","테스트 제목", "테스트 메세지", "썸네일 url", buttons);
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }

    @Test
    void createSimpleText() throws ParseException {
        JSONObject result = kakaoApiService.createSimpleText("심플텍스트 메세지");
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }

    @Test
    void createSimpleImage() throws ParseException {

        JSONObject result = kakaoApiService.createSimpleImage("altText테스트", "www.asdsad.com");
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }

    @Test
    void createCommerceCard() throws ParseException {
        List<Button> buttons = new ArrayList<>();
        Button button = new Button("action","label","www.asdasd.com");
        buttons.add(button);
        JSONObject result = kakaoApiService.createCommerceCard(
                "basic",
                "desc",
                10000,
                1000,
                0,
                0,
                "won",
                "www.asdasdasd",
                "www.asdasdzzxcz..zxc",
                "www.avbc13213",
                "dsadadasd",
                buttons);
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);

    }


    @Test
    void createDeveloperInfo() throws ParseException {
        JSONObject result = kakaoApiService.createDeveloperInfo();
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }


    @Test
    void createLocationNotice() throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        String kakaoUserkey = "21321312";
        kakaoResponse.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
        JSONObject result = kakaoResponse.createKakaoResponse();
        log.info("result={},",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }
    @Test
    void todayWeather() throws Exception {
        KakaoResponse kakaoResponse = new KakaoResponse();
        String kakaoUserkey= "QFERwysZbO77";

        kakaoResponse.addContent( kakaoApiService.createTodayWeather(kakaoUserkey));
        JSONObject result = kakaoResponse.createKakaoResponse();
        log.info("result={},",result);
        assertThat(result).isInstanceOf(JSONObject.class);

    }

    @Test
    void createRecommendItems() throws Exception {
        JSONObject recommendItems = kakaoApiService.createRecommendItems();
        KakaoResponse kakaoResponse = new KakaoResponse();
        kakaoResponse.addContent(recommendItems);
        log.info("result={}",kakaoResponse.createKakaoResponse());

    }

    @Test
    void createListCard() throws Exception {
//        List<ListCardItem> listCardItems = new ArrayList<>();
//        ListCardItem listCardItem = new ListCardItem("title","desc","img","webl");
//        listCardItems.add(listCardItem);
//        List<Button> buttons = new ArrayList<>();
//        Button button = new Button("ac","la","we");
//        buttons.add(button);
//        JSONObject recommendItems = kakaoApiService.createListCard("","테스트",listCardItems,buttons);
//        KakaoResponse kakaoResponse = new KakaoResponse();
//        kakaoResponse.addContent(recommendItems);
//        log.info("result={}",kakaoResponse.createKakaoResponse());

    }

    @Test
    void createTodayNews() throws Exception {
        KakaoResponse kakaoResponse = new KakaoResponse();
        kakaoResponse.addContent(kakaoApiService.createTodayNews("뉴스"));
        log.info("result={}",  kakaoResponse.createKakaoResponse()       );
    }
}