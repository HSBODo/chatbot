package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.kakaoui.*;

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
        List<ButtonDto> buttonDtos = new ArrayList();
        ButtonDto buttonDto = new ButtonDto(ButtonAction.webLink,"laber","www.asdasd.com");
        ButtonDto buttonDto2 = new ButtonDto(ButtonAction.webLink,"label1","www.asdasd.com1");
        buttonDtos.add(buttonDto);
        buttonDtos.add(buttonDto2);
        JSONObject result = kakaoJsonUiService.createBasicCard(DisplayType.basic,"테스트 제목", "테스트 메세지", "썸네일 url", buttonDtos);
        log.info("basicCard={}",result);
    }

    @Test
    void createCommerceCard() throws ParseException {
        List<ButtonDto> buttonDtos = new ArrayList<>();
        ButtonDto buttonDto = new ButtonDto(ButtonAction.webLink,"label","www.asdasd.com");
        buttonDtos.add(buttonDto);
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
                buttonDtos);
        log.info("commerceCard={}",result);
    }

    @Test
    void createListCard() throws Exception {
        List<ListCardItemDto> listCardItemDtos = new ArrayList<>();
        Map<String,String> link = new HashMap<>();
        link.put("web","asdsadsa");
        ListCardItemDto listCardItemDto = new ListCardItemDto("title","desc","img",link);
        listCardItemDtos.add(listCardItemDto);
        List<ButtonDto> buttonDtos = new ArrayList<>();
        ButtonDto buttonDto = new ButtonDto(ButtonAction.webLink,"la","we");
        buttonDtos.add(buttonDto);
        JSONObject recommendItems = kakaoJsonUiService.createListCard(DisplayType.basic,"123123", listCardItemDtos, buttonDtos);
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        kakaoResponseDto.addContent(recommendItems);
        log.info("listCard={}", kakaoResponseDto.createKakaoResponse());

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