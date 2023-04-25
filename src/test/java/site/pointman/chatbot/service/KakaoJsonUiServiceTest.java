package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.KakaoResponseDto;
import site.pointman.chatbot.dto.kakaoui.ListCardItemDto;

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
        ButtonDto buttonDto = new ButtonDto("action","laber","www.asdasd.com");
        ButtonDto buttonDto2 = new ButtonDto("action1","label1","www.asdasd.com1");
        buttonDtos.add(buttonDto);
        buttonDtos.add(buttonDto2);
        JSONObject result = kakaoJsonUiService.createBasicCard("","테스트 제목", "테스트 메세지", "썸네일 url", buttonDtos);
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }

    @Test
    void createCommerceCard() throws ParseException {
        List<ButtonDto> buttonDtos = new ArrayList<>();
        ButtonDto buttonDto = new ButtonDto("action","label","www.asdasd.com");
        buttonDtos.add(buttonDto);
        JSONObject result = kakaoJsonUiService.createCommerceCard(
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
                buttonDtos);
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);

    }

    @Test
    void createListCard() throws Exception {
        List<ListCardItemDto> listCardItemDtos = new ArrayList<>();
        Map<String,String> link = new HashMap<>();
        link.put("web","asdsadsa");
        ListCardItemDto listCardItemDto = new ListCardItemDto("title","desc","img",link);
        listCardItemDtos.add(listCardItemDto);
        List<ButtonDto> buttonDtos = new ArrayList<>();
        ButtonDto buttonDto = new ButtonDto("ac","la","we");
        buttonDtos.add(buttonDto);
        JSONObject recommendItems = kakaoJsonUiService.createListCard("","123123", listCardItemDtos, buttonDtos);
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto();
        kakaoResponseDto.addContent(recommendItems);
        log.info("result={}", kakaoResponseDto.createKakaoResponse());

    }


    @Test
    void createCarousel() {
    }

    @Test
    void createSimpleText() throws ParseException {
        JSONObject result = kakaoJsonUiService.createSimpleText("심플텍스트 메세지");
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }
    @Test
    void createSimpleImage() throws ParseException {

        JSONObject result = kakaoJsonUiService.createSimpleImage("altText테스트", "www.asdsad.com");
        log.info("result={}",result);
        assertThat(result).isInstanceOf(JSONObject.class);
    }
}