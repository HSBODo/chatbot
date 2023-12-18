package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.annotation.SkipLogging;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.common.*;
import site.pointman.chatbot.domain.response.property.components.*;

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
    public ChatBotResponse createSimpleText() {
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
    public ChatBotResponse createSimpleImg() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        chatBotResponse.addSimpleImage("https://www.youtube.com/","유튜브 이미지");
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "textCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createTextCard() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        TextCard textCard = new TextCard();
        textCard.setTitle("제목");
        textCard.setDescription("설명");

        chatBotResponse.addTextCard(textCard);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "basicCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createBasicCard() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        Extra extra = new Extra();
        extra.addChoiceParam("선택 파라미터");

        BasicCard basicCard = new BasicCard();
        basicCard.setThumbnail("URL");
        basicCard.setBlockButton("블록버튼","blockId",extra);

        chatBotResponse.addBasicCard(basicCard);
        chatBotResponse.addQuickButton(ButtonName.이전으로,"블록아이디",extra);

        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "commerceCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createCommerceCard() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        CommerceCard commerceCard = new CommerceCard();
        commerceCard.setThumbnails("https://",true);

        chatBotResponse.addCommerceCard(commerceCard);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "listCard" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createListCard() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        ListItem listItem = new ListItem("제목");

        Button button = new Button();
        button.createBlockButton("asdasd","asdasd");

        ListCard listCard = new ListCard();
        listCard.setHeader("리스트카드 헤더 제목");
        listCard.setItem(listItem);
        listCard.setButtons(button);

        chatBotResponse.addListCard(listCard);
        return chatBotResponse;
    }

    @ResponseBody
    @PostMapping(value = "carousel" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse createCarousel() {
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
