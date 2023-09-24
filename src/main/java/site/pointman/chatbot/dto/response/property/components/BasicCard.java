package site.pointman.chatbot.dto.response.property.components;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.common.Button;
import site.pointman.chatbot.dto.response.property.common.Profile;
import site.pointman.chatbot.dto.response.property.common.Thumbnail;

import java.util.List;

@Getter
@Setter
public class BasicCard {
    private String title;
    private String description;
    private Thumbnail thumbnail;
    private Profile profile;
    private List<Button> buttons;

}
