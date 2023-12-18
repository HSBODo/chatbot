package site.pointman.chatbot.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.domain.response.property.Component;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.Data;
import site.pointman.chatbot.domain.response.property.Template;
import site.pointman.chatbot.domain.response.property.common.*;
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

    public void addQuickButton(String buttonName, String blockId, Extra extra){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName,blockId,extra);
        this.template.getQuickReplies().add(quickReplyButton);
    }

    public void addQuickButton(String buttonName, String blockId){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName,blockId);
        this.template.getQuickReplies().add(quickReplyButton);
    }

    public void addQuickButton(String buttonName){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName);
        this.template.getQuickReplies().add(quickReplyButton);
    }

    public void addQuickButton(Category buttonName, String blockId, Extra extra){
        String name = buttonName.getValue();
        addQuickButton(name,blockId,extra);
    }

    public void addQuickButton(ButtonName buttonName, String blockId, Extra extra){
        String name = buttonName.name();
        addQuickButton(name,blockId,extra);
    }

    public void addQuickButton(ButtonName buttonName, String blockId){
        String name = buttonName.name();
        addQuickButton(name,blockId);
    }

    public void addQuickButton(ButtonName buttonName){
        String name = buttonName.name();
        addQuickButton(name);
    }

    public void addSimpleText(String text){
        SimpleText simpleText = new SimpleText();
        simpleText.setText(text);
        Component component = new Component(simpleText);

        this.template.getOutputs().add(component);
    }

    public void addSimpleImage(String imgUrl, String altText){
        SimpleImage simpleImage =new SimpleImage();
        simpleImage.setImageUrl(imgUrl);
        simpleImage.setAltText(altText);
        Component component = new Component(simpleImage);

        this.template.getOutputs().add(component);
    }
    public void addTextCard(String title, String text, Buttons buttons){
        if(buttons.getButtons().size()>3){
            throw new IllegalArgumentException("버튼은 최대 3개까지만 추가할 수 있습니다.");
        }
        TextCard textCard = new TextCard();
        textCard.setTitle(title);
        textCard.setText(text);
        textCard.setButtons(buttons.getButtons());
        Component component = new Component(textCard);

        this.template.getOutputs().add(component);
    }

    public void addTextCard(String title,String text){
        TextCard textCard = new TextCard();
        textCard.setTitle(title);
        textCard.setText(text);
        Component component = new Component(textCard);

        this.template.getOutputs().add(component);
    }

    public void addTextCard(String text){
        TextCard textCard = new TextCard();
        textCard.setText(text);
        Component component = new Component(textCard);

        this.template.getOutputs().add(component);
    }

    public void addBasicCard(String title, String description, String thumbnailImgUrl,String profileImgUrl,String profileNickname, Buttons buttons){
        if(thumbnailImgUrl.isBlank()){
            throw new IllegalArgumentException("썸네일 이미지 URL은 필수입니다.");
        }


        if(buttons.getButtons().size()>3){
            throw new IllegalArgumentException("버튼은 최대 3개까지만 추가할 수 있습니다.");
        }

        BasicCard basicCard = new BasicCard();
        if(!profileImgUrl.isBlank() && !profileNickname.isBlank() ){
            basicCard.setProfile(profileNickname, profileImgUrl);
        }

        basicCard.setTitle(title);
        basicCard.setDescription(description);
        basicCard.setThumbnail(thumbnailImgUrl);
        basicCard.setButtons(buttons.getButtons());

        Component component = new Component(basicCard);
        this.template.getOutputs().add(component);
    }

    public void addCommerceCard(String title, String description, int price, int discount, int discountRate , int dicountedPrice , String thumbnailImgUrl, String profileImgUrl,String profileNickname, Buttons buttons ){
        /*
        Information. price, discount, discountedPrice 의 동작 방식
        discountedPrice 가 존재하면 price, discount, discountRate 과 관계 없이 무조건 해당 값이 사용자에게 노출됩니다.
        예) price: 10000, discount: 7000, discountedPrice: 2000 인 경우, 3000 (10000 - 7000)이 아닌 2000이 사용자에게 노출
        위의 예에서 discountedPrice가 없는 경우, 3000이 사용자에게 노출
        예) price: 10000, discountRate: 70, discountedPrice: 2000 인 경우, 3000 (10000 * 0.3)이 아닌 2000이 사용자에게 노출
        discountRate은 discountedPrice를 필요로 합니다. discountedPrice가 주어지지 않으면 사용자에게 >discountRate을 노출하지 않습니다.
        discountRate과 discount가 동시에 있는 경우, discountRate을 우선적으로 노출합니다.
        */
        if(thumbnailImgUrl.isBlank()){
            throw new IllegalArgumentException("썸네일 이미지 URL은 필수입니다.");
        }
        if(buttons.getButtons().size()>3){
            throw new IllegalArgumentException("버튼은 최대 3개까지만 추가할 수 있습니다.");
        }


        CommerceCard commerceCard = new CommerceCard();

        if(!profileImgUrl.isBlank() && !profileNickname.isBlank() ){
            Profile profile = new Profile(profileNickname,profileImgUrl);
            commerceCard.setProfile(profile);
        }
        Thumbnail thumbnail = new Thumbnail(thumbnailImgUrl);

        commerceCard.setTitle(title);
        commerceCard.setDescription(description);
        commerceCard.setPrice(price);
        commerceCard.setDiscount(discount);
        commerceCard.setDiscountRate(discountRate);
        commerceCard.setDiscountedPrice(dicountedPrice);
        commerceCard.setThumbnails(thumbnail);
        commerceCard.setButtons(buttons);

        Component component = new Component(commerceCard);
        this.template.getOutputs().add(component);
    }

    public void addCommerceCard(String title, String description, int price, int discount, String thumbnailImgUrl, String profileImgUrl,String profileNickname, Buttons buttons ){
        /*
        Information. price, discount, discountedPrice 의 동작 방식
        discountedPrice 가 존재하면 price, discount, discountRate 과 관계 없이 무조건 해당 값이 사용자에게 노출됩니다.
        예) price: 10000, discount: 7000, discountedPrice: 2000 인 경우, 3000 (10000 - 7000)이 아닌 2000이 사용자에게 노출
        위의 예에서 discountedPrice가 없는 경우, 3000이 사용자에게 노출
        예) price: 10000, discountRate: 70, discountedPrice: 2000 인 경우, 3000 (10000 * 0.3)이 아닌 2000이 사용자에게 노출
        discountRate은 discountedPrice를 필요로 합니다. discountedPrice가 주어지지 않으면 사용자에게 >discountRate을 노출하지 않습니다.
        discountRate과 discount가 동시에 있는 경우, discountRate을 우선적으로 노출합니다.
        */
        if(thumbnailImgUrl.isBlank()){
            throw new IllegalArgumentException("썸네일 이미지 URL은 필수입니다.");
        }
        if(buttons.getButtons().size()>3){
            throw new IllegalArgumentException("버튼은 최대 3개까지만 추가할 수 있습니다.");
        }


        CommerceCard commerceCard = new CommerceCard();

        if(!profileImgUrl.isBlank() && !profileNickname.isBlank() ){
            Profile profile = new Profile(profileNickname,profileImgUrl);
            commerceCard.setProfile(profile);
        }

        Thumbnail thumbnail = new Thumbnail(thumbnailImgUrl);
;
        commerceCard.setTitle(title);
        commerceCard.setDescription(description);
        commerceCard.setPrice(price);
        commerceCard.setDiscount(discount);
        commerceCard.setThumbnails(thumbnail);
        commerceCard.setButtons(buttons);

        Component component = new Component(commerceCard);
        this.template.getOutputs().add(component);
    }

    public void addListCard(ListCard listCard ){
        if(listCard.getItems().size()==0){
            throw new IllegalArgumentException("아이템의 최소 개수는 1개 입니다.");
        }

        Component component = new Component(listCard);
        this.template.getOutputs().add(component);
    }

    public void addCarousel(Carousel carousel ){
        Component component = new Component(carousel);
        this.template.getOutputs().add(component);
    }

    public void addContext(Context context ){
        this.context.addContext(context);
    }


}
