package site.pointman.chatbot.dto.response.property.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListItems {
    private List<ListItem> listItems = new ArrayList<>();

    public void addLinkItem(String title,String description,String imgUrl,String linkUrl){
        ListItem listItem = new ListItem();
        Link link = new Link();
        link.setWeb(linkUrl);
        listItem.setTitle(title);
        listItem.setDescription(description);
        listItem.setImageUrl(imgUrl);
        listItem.setLink(link);

        this.listItems.add(listItem);
    }
    public void addBlockItem(String title, String description, String imgUrl, String blockId, Extra extra){

        ListItem listItem = new ListItem();
        listItem.setTitle(title);
        listItem.setDescription(description);
        listItem.setImageUrl(imgUrl);
        listItem.setAction("block");
        listItem.setBlockId(blockId);
        if(extra!=null){
            listItem.setExtra(extra.getExtra());
        }

        this.listItems.add(listItem);
    }
    public void addMessageItem(String title,String description,String imgUrl,String messageText,Extra extra){
        ListItem listItem = new ListItem();
        listItem.setTitle(title);
        listItem.setDescription(description);
        listItem.setImageUrl(imgUrl);
        listItem.setAction("message");
        listItem.setMessageText(messageText);
        if(extra!=null){
            listItem.setExtra(extra.getExtra());
        }
        this.listItems.add(listItem);
    }
}
