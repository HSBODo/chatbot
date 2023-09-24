package site.pointman.chatbot.dto.response.property.components;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.common.Button;
import site.pointman.chatbot.dto.response.property.common.Profile;
import site.pointman.chatbot.dto.response.property.common.Thumbnail;

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
    private int dicountedPrice;
    private List<Thumbnail> thumbnails;
    private Profile profile;
    private List<Button> buttons;

}
