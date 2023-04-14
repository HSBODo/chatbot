package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.kakaochatbotui.*;
import site.pointman.chatbot.domain.KakaoResponse;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KaKaoItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.serviceimpl.KakaoApiServiceImpl;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class KakaoApiServiceTest {
    @Autowired
    private BasicCard basicCard;
    @Autowired
    private SimpleText simpleText;
    @Autowired
    private  SimpleImage simpleImage;
    @Autowired
    private WeatherApiService weatherApiService;
    @Autowired
    private CommerceCard commerceCard;
    @Autowired
    private KakaoApiService kakaoApiService ;
    @Autowired
    private KakaoMemberRepository kakaoMemberRepository;

    @Autowired
    private KaKaoItemRepository kaKaoItemRepository;
    @Autowired
    private Carousel carousel;



    @PostConstruct
    void init(){
        this.kakaoApiService = new KakaoApiServiceImpl(kaKaoItemRepository,kakaoMemberRepository,basicCard,simpleText,simpleImage,weatherApiService,commerceCard,carousel);
    }


    @Test
    void createBasicCard() throws ParseException {
        Buttons buttons = new Buttons();
        Button button = new Button("action","laber","www.asdasd.com");
        buttons.addButton(button);
        JSONObject result = kakaoApiService.createBasicCard("테스트 제목", "테스트 메세지", "썸네일 url", buttons);
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
        Buttons buttons = new Buttons();
        Button button = new Button("action","label","www.asdasd.com");
        buttons.addButton(button);
        JSONObject result = kakaoApiService.createCommerceCard(
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
    void createListCard() {
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
}