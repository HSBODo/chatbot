package site.pointman.chatbot.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.constant.ButtonAction;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.ButtonParamKey;
import site.pointman.chatbot.domain.response.property.Component;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.Data;
import site.pointman.chatbot.domain.response.property.Template;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.Values;
import site.pointman.chatbot.domain.response.property.components.*;


@Getter
public class ChatBotResponse extends Response {
    private String version;
    private Template template;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Values context;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Data data;

    public ChatBotResponse() {
        this.version ="2.0";
        this.template = new Template();
        this.context = new Values();
    }

    public void addQuickButton(String buttonName, ButtonAction buttonAction, String actionValue, ButtonParamKey buttonParamKey, String buttonParamValue){
        Button button = new Button(buttonName, buttonAction, actionValue, buttonParamKey, buttonParamValue);
        this.template.getQuickReplies().add(button);
    }

    public void addQuickButton(String buttonName, ButtonAction buttonAction, String actionValue){
        Button button = new Button(buttonName, buttonAction, actionValue);
        this.template.getQuickReplies().add(button);
    }

    public void addQuickButton(String buttonName){
        Button button = new Button(buttonName);
        this.template.getQuickReplies().add(button);
    }

    public void addQuickButton(ButtonName buttonName){
        String name = buttonName.name();
        addQuickButton(name);
    }
    public void addQuickButton(Button button){
        this.template.getQuickReplies().add(button);
    }

    public void addSimpleText(String text){
        SimpleText simpleText = new SimpleText(text);
        Component component = new Component(simpleText);

        this.template.getOutputs().add(component);
    }

    public void addSimpleText(SimpleText simpleText){
        Component component = new Component(simpleText);

        this.template.getOutputs().add(component);
    }

    public void addSimpleImage(String imgUrl, String altText){
        SimpleImage simpleImage =new SimpleImage(imgUrl, altText);
        Component component = new Component(simpleImage);

        this.template.getOutputs().add(component);
    }

    public void addTextCard(TextCard textCard){
        Component component = new Component(textCard);

        this.template.getOutputs().add(component);
    }

    public void addTextCard(String title, String description){
        TextCard textCard = new TextCard();
        textCard.setTitle(title);
        textCard.setDescription(description);

        Component component = new Component(textCard);

        this.template.getOutputs().add(component);
    }

    public void addBasicCard(BasicCard basicCard){
        if(basicCard.getThumbnail().getImageUrl().isBlank()) throw new IllegalArgumentException("썸네일 이미지 URL은 필수입니다.");

        Component component = new Component(basicCard);

        this.template.getOutputs().add(component);
    }

    public void addCommerceCard(CommerceCard commerceCard){
        if(commerceCard.getThumbnails().size() != 1) throw new IllegalArgumentException("썸네일 이미지 URL은 최소 1개가 필수입니다. 현재 1개만 지원");

        if(commerceCard.getButtons().size() == 0) throw new IllegalArgumentException("버튼은 최소 1개 이상이 필수입니다.");

        Component component = new Component(commerceCard);

        this.template.getOutputs().add(component);
    }

    public void addListCard(ListCard listCard){
        if(listCard.getItems().size()==0) throw new IllegalArgumentException("아이템의 최소 개수는 1개 입니다.");

        Component component = new Component(listCard);

        this.template.getOutputs().add(component);
    }

    public void addCarousel(Carousel carousel){
        Component component = new Component(carousel);

        this.template.getOutputs().add(component);
    }

    public void addContext(Context context ){
        this.context.addContext(context);
    }
}
