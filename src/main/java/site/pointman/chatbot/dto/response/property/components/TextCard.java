package site.pointman.chatbot.dto.response.property.components;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.common.Button;

import java.util.List;
@Getter
@Setter
public class TextCard {
    private String title;
    private String text;
    private List<Button> buttons;
}
