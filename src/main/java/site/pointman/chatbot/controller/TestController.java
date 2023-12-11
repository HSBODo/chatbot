package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.Context;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
import site.pointman.chatbot.dto.response.property.common.Buttons;
import site.pointman.chatbot.dto.response.property.common.Extra;
import site.pointman.chatbot.dto.response.property.common.ListItems;
import site.pointman.chatbot.dto.response.property.components.BasicCard;
import site.pointman.chatbot.dto.response.property.components.Carousel;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequestMapping(value = "/test/*")
public class TestController {
    @ResponseBody
    @PostMapping(value = "test" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto test(HttpServletRequest httpRequest) throws Exception {
        ServletInputStream inputStream = httpRequest.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("http request = {}", messageBody);

        try {

            ResponseDto responseDto = new ResponseDto();
            Context context = new Context("session",3,600);
            context.addParam("test","12312");
            Context context2 = new Context("test",3,600);
            context2.addParam("asdasdccd","99999999");
            QuickReplyButtons quickReplyButtons =new QuickReplyButtons();
            Extra extra = new Extra();
            extra.addChoiceParam("선택 파라미터");
            quickReplyButtons.addBlockQuickButton("테스트2","65140667e6fda240f3fedc4f",extra);


            responseDto.addSimpleText("심플텍스트 테스트");
            responseDto.addQuickButton(quickReplyButtons);
            responseDto.addContext(context);
            responseDto.addContext(context2);
            return responseDto;
        }catch (Exception e){
            log.info("exception = {}",e.getMessage());
            return null;
        }

    }

    @ResponseBody
    @PostMapping(value = "simpleText" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createSimpleText(@RequestBody RequestDto request, HttpServletRequest httpRequest) throws Exception {
        try {
            ResponseDto responseDto = new ResponseDto();
            responseDto.addSimpleText("심플텍스트 테스트");
            return responseDto;
        }catch (Exception e){
            log.info("exception = {}",e.getMessage());
            log.info("exception = {}",e.getMessage());
            return null;
        }

    }

    @ResponseBody
    @PostMapping(value = "simpleImg" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createSimpleImg(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        responseDto.addSimpleImage("https://www.youtube.com/","유튜브 이미지");
        return responseDto;
    }

    @ResponseBody
    @PostMapping(value = "textCard" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createTextCard(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        Buttons buttons = new Buttons();
        buttons.addWebLinkButton("웹링크버튼","https://www.youtube.com/");
        responseDto.addTextCard("텍스트카드 테스트",buttons);
        return responseDto;
    }

    @ResponseBody
    @PostMapping(value = "basicCard" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createBasicCard(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        Buttons buttons = new Buttons();
        QuickReplyButtons quickReplyButtons = new QuickReplyButtons();
        Extra extra = new Extra();
        extra.addChoiceParam("선택 파라미터");
        quickReplyButtons.addBlockQuickButton("블록퀵버튼","블록아이디",extra);
        buttons.addBlockButton("블록버튼","danklnclsv123115vdf");
        responseDto.addBasicCard("제목","설명","섬네일 URL https://www.youtube.com/","","",buttons);
        responseDto.addQuickButton(quickReplyButtons);
        return responseDto;
    }

    @ResponseBody
    @PostMapping(value = "commerceCard" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createCommerceCard(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        Buttons buttons = new Buttons();
        buttons.addPhoneButton("전화버튼","01000000000");
        responseDto.addCommerceCard("제목","설명",10000,50,"123123","","",buttons);
        return responseDto;
    }

    @ResponseBody
    @PostMapping(value = "listCard" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createListCard(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        Buttons buttons = new Buttons();
        buttons.addPhoneButton("전화버튼","01000000000");
        ListItems listItems = new ListItems();
        listItems.addBlockItem("소제목","설명","이미지URL","블록아이디", null);
        listItems.addLinkItem("소제목","설명","이미지URL","클릭시 이동링크");
        listItems.addMessageItem("소제목","설명","이미지URL","발화내용", null);
        responseDto.addListCard("리스트카드 대제목",listItems,buttons);
        return responseDto;
    }

    @ResponseBody
    @PostMapping(value = "carousel" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createCarousel(@RequestBody RequestDto request)throws Exception {
        ResponseDto responseDto = new ResponseDto();
        Carousel<BasicCard> carousel = new Carousel<>();
        BasicCard basicCard1 = new BasicCard();
        basicCard1.setTitle("1");
        BasicCard basicCard2 = new BasicCard();
        basicCard2.setTitle("2");
        carousel.addComponent(basicCard1);
        carousel.addComponent(basicCard2);
        responseDto.addCarousel(carousel);
        return responseDto;
    }

}
