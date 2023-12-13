package site.pointman.chatbot.domain.response.property.components;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.ListItem;

import java.util.List;

@Getter
@Setter
public class ListCard {
    private ListItem header;
    private List<ListItem> items;
    private List<Button> buttons;
}
