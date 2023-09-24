package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
import site.pointman.chatbot.dto.response.property.common.Buttons;
import site.pointman.chatbot.dto.response.property.common.Extra;
import site.pointman.chatbot.dto.response.property.common.ListItems;
import site.pointman.chatbot.dto.response.property.components.BasicCard;
import site.pointman.chatbot.dto.response.property.components.Carousel;

@Slf4j
@Controller
@RequestMapping(value = "/test/*")
public class TestController {

    @ResponseBody
    @PostMapping(value = "simpleText" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto createSimpleText(@RequestBody RequestDto request) throws Exception {
        ResponseDto responseDto = new ResponseDto();
        responseDto.addSimpleText("심플텍스트 테스트");
        return responseDto;
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
