package site.pointman.chatbot.domain.response.property.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.constant.BlockId;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ListItem {
    private String title;
    private String description;
    private String imageUrl;
    private Link link;
    private String action;
    private String blockId;
    private String messageText;
    private Map<String,String> extra = new HashMap<>();

    public ListItem(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void setActionBlock(BlockId nextBlockId) {
        this.action = "block";
        this.blockId = nextBlockId.getBlockId();
    }

    public void setActionMessage(String messageText) {
        this.action = "message";
        this.messageText = messageText;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public void setExtra(String key, String value) {
        this.extra.put(key,value);
    }
}
