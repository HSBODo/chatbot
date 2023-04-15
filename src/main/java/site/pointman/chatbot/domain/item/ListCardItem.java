package site.pointman.chatbot.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ListCardItem {
    private String itemTitle;
    private String description;
    private String imgUrl;
    private String webLink;


}

