package site.pointman.chatbot.dto.response.property;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.dto.response.property.components.*;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Component {
   private SimpleText simpleText;
   private SimpleImage simpleImage;
   private TextCard textCard;
   private BasicCard basicCard;
   private CommerceCard commerceCard;
   private ListCard listCard;
   private Carousel carousel;

    public Component(SimpleText simpleText) {
        this.simpleText = simpleText;
    }

    public Component(SimpleImage simpleImage) {
        this.simpleImage = simpleImage;
    }

    public Component(TextCard textCard) {
        this.textCard = textCard;
    }

    public Component(BasicCard basicCard) {
        this.basicCard = basicCard;
    }

    public Component(CommerceCard commerceCard) {
        this.commerceCard = commerceCard;
    }

    public Component(ListCard listCard) {
        this.listCard = listCard;
    }
    public Component(Carousel carousel) {
        this.carousel = carousel;
    }
}
