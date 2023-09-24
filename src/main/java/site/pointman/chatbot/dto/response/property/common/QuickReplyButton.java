package site.pointman.chatbot.dto.response.property.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class QuickReplyButton {
    private String action;
    private String label;

    private String blockId;
    private String messageText;
    private Map<String,String> extra;

    public void createBlockQuickButton(String quickButtonName,String blockId, Extra extra){
        this.action = "block";
        this.label = quickButtonName;
        this.blockId = blockId;
        if(extra!=null){
            this.extra=extra.getExtra();
        }
    }

    public void createMessageQuickButton(String quickButtonName,String messageText, Extra extra){
        this.action = "block";
        this.label = quickButtonName;
        this.messageText = messageText;
        if(extra!=null){
            this.extra=extra.getExtra();
        }
    }
}
