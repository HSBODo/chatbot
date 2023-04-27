package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.vo.*;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.CarouselType;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ListCardItemVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class KakaoJsonUiServiceTest {
    @Autowired
    private KakaoJsonUiService kakaoJsonUiService;

    @Test
    void createBasicCard() throws ParseException {
        List<ButtonVo> buttonVos = new ArrayList();
        ButtonVo buttonVo = new ButtonVo("action","laber","www.asdasd.com");
        ButtonVo buttonVo2 = new ButtonVo("action1","label1","www.asdasd.com1");
        buttonVos.add(buttonVo);
        buttonVos.add(buttonVo2);
        JSONObject result = kakaoJsonUiService.createBasicCard(DisplayType.basic,"테스트 제목", "테스트 메세지", "썸네일 url", buttonVos);
        log.info("basicCard={}",result);
    }

    @Test
    void createCommerceCard() throws ParseException {
        List<ButtonVo> buttonVos = new ArrayList<>();
        ButtonVo buttonVo = new ButtonVo("action","label","www.asdasd.com");
        buttonVos.add(buttonVo);
        JSONObject result = kakaoJsonUiService.createCommerceCard(
                DisplayType.basic,
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
                buttonVos);
        log.info("commerceCard={}",result);
    }

    @Test
    void createListCard() throws Exception {
        List<ListCardItemVo> listCardItemVos = new ArrayList<>();
        Map<String,String> link = new HashMap<>();
        link.put("web","asdsadsa");
        ListCardItemVo listCardItemVo = new ListCardItemVo("title","desc","img",link);
        listCardItemVos.add(listCardItemVo);
        List<ButtonVo> buttonVos = new ArrayList<>();
        ButtonVo buttonVo = new ButtonVo("ac","la","we");
        buttonVos.add(buttonVo);
        JSONObject recommendItems = kakaoJsonUiService.createListCard(DisplayType.basic,"123123", listCardItemVos, buttonVos);
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        kakaoResponseVo.addContent(recommendItems);
        log.info("listCard={}", kakaoResponseVo.createKakaoResponse());

    }


    @Test
    void createCarousel() throws ParseException {
        List items = new ArrayList<>();
        JSONObject basicCard1 = kakaoJsonUiService.createBasicCard(DisplayType.carousel, "테스트케로셀1", "베이직카드", "섭네일링크", null);
        JSONObject basicCard2 = kakaoJsonUiService.createBasicCard(DisplayType.carousel, "테스트케로셀2", "베이직카드", "섭네일링크", null);
        JSONObject basicCard3 = kakaoJsonUiService.createBasicCard(DisplayType.carousel, "테스트케로셀3", "베이직카드", "섭네일링크", null);
        JSONObject basicCard4 = kakaoJsonUiService.createBasicCard(DisplayType.carousel, "테스트케로셀4", "베이직카드", "섭네일링크", null);
        items.add(basicCard1);
        items.add(basicCard2);
        items.add(basicCard3);
        items.add(basicCard4);
        JSONObject carousel = kakaoJsonUiService.createCarousel(CarouselType.basicCard, items);
        log.info("carousel={}",carousel);

    }

    @Test
    void createSimpleText() throws ParseException {
        JSONObject result = kakaoJsonUiService.createSimpleText("심플텍스트 메세지");
        log.info("simpleText={}",result);
    }
    @Test
    void createSimpleImage() throws ParseException {
        JSONObject result = kakaoJsonUiService.createSimpleImage("altText테스트", "www.asdsad.com");
        log.info("simpleImage={}",result);
    }
}