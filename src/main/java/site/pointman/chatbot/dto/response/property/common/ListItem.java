package site.pointman.chatbot.dto.response.property.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.common.Link;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ListItem {
    private String title;
    private String description;
    private String imageUrl;
    private Link link;
    private String action;
    private String blockId;
    private String messageText;
    private Map<String,String> extra;
}
