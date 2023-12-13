package site.pointman.chatbot.domain.response.property.components;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.Buttons;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.domain.response.property.common.Thumbnail;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CommerceCard {
    private String title;
    private String description;
    private int price;
    private String currency = "won";
    private int discount;
    private int discountRate;
    private int discountedPrice;
    private List<Thumbnail> thumbnails;
    private Profile profile;
    private List<Button> buttons;


    public CommerceCard() {
        this.thumbnails = new ArrayList<>();
    }

    public void setThumbnails(Thumbnail thumbnail){
        this.thumbnails.add(thumbnail);
    }

    public void setButtons(Buttons buttons) {
        this.buttons = buttons.getButtons();
    }
}
