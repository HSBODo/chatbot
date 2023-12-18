package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.annotation.SkipLogging;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.common.Buttons;
import site.pointman.chatbot.domain.response.property.common.Extra;
import site.pointman.chatbot.domain.response.property.common.ListItems;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping(value = "/test/*")
public class TestController {

    @ResponseBody
    @SkipLogging
    @PostMapping(value = "json" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse test(@RequestBody String request) throws Exception {
        log.info("http request = {}", request);
        return null;
    }

    @ResponseBody
    @PostMapping(value = "simpleText" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createSimpleText(@RequestBody ChatBotRequest request, HttpServletRequest httpRequest) throws Exception {
        try {
            ChatBotResponse chatBotResponse = new ChatBotResponse();
            chatBotResponse.addSimpleText("심플텍스트 테스트");
            return chatBotResponse;
        }catch (Exception e){
            log.info("exception = {}",e.getMessage());
            log.info("exception = {}",e.getMessage());
            return null;
        }

    }

    @ResponseBody
    @PostMapping(value = "simpleImg" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createSimpleImg(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        chatBotResponse.addSimpleImage("https://www.youtube.com/","유튜브 이미지");
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "textCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createTextCard(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Buttons buttons = new Buttons();
        buttons.addWebLinkButton("웹링크버튼","https://www.youtube.com/");
        chatBotResponse.addTextCard("제목","텍스트카드 테스트",buttons);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "basicCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createBasicCard(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Buttons buttons = new Buttons();
        Extra extra = new Extra();
        extra.addChoiceParam("선택 파라미터");

        buttons.addBlockButton("블록버튼","danklnclsv123115vdf");
        chatBotResponse.addBasicCard("제목","설명","섬네일 URL https://www.youtube.com/","","",buttons);
        chatBotResponse.addQuickButton(ButtonName.이전으로,"블록아이디",extra);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "commerceCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createCommerceCard(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Buttons buttons = new Buttons();
        buttons.addPhoneButton("전화버튼","01000000000");
        chatBotResponse.addCommerceCard("제목","설명",10000,50,"123123","","",buttons);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "listCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createListCard(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Buttons buttons = new Buttons();
        buttons.addPhoneButton("전화버튼","01000000000");
        ListItems listItems = new ListItems();
        listItems.addBlockItem("소제목","설명","이미지URL","블록아이디", null);
        listItems.addLinkItem("소제목","설명","이미지URL","클릭시 이동링크");
        listItems.addMessageItem("소제목","설명","이미지URL","발화내용", null);
        chatBotResponse.addListCard("리스트카드 대제목",listItems,buttons);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "carousel" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createCarousel(@RequestBody ChatBotRequest request)throws Exception {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<BasicCard> carousel = new Carousel<>();
        BasicCard basicCard1 = new BasicCard();
        basicCard1.setTitle("1");
        BasicCard basicCard2 = new BasicCard();
        basicCard2.setTitle("2");
        carousel.addComponent(basicCard1);
        carousel.addComponent(basicCard2);
        chatBotResponse.addCarousel(carousel);
        return chatBotResponse;
    }

}
