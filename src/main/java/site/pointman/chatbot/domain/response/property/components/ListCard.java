package site.pointman.chatbot.domain.response.property.components;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.ListItem;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListCard {
    private ListItem header;
    private List<ListItem> items = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();

    public void setHeader(String title) {
        ListItem listItem = new ListItem(title);
        this.header = (listItem);
    }

    public void setItem(ListItem item) {
        if(items.size()>5) throw new IllegalArgumentException("아이템의 최대 개수는 5개 입니다.");

        this.items.add(item);
    }

    public void setButtons(Button button) {
        if(buttons.size()>2) throw new IllegalArgumentException("버튼은 최대 2개까지만 추가할 수 있습니다.");

        this.buttons.add(button);
    }
}
