package site.pointman.chatbot.dto.response.property.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Button {
    private String action;
    private String label;

    private String webLinkUrl;
    private String blockId;
    private String phoneNumber;
    private String messageText;

    public void createWebLinkButton(String buttonName,String webLinkUrl){
        this.action = "webLink";
        this.label = buttonName;
        this.webLinkUrl = webLinkUrl;
    }

    public void createBlockButton(String buttonName,String blockId){
        this.action = "block";
        this.label = buttonName;
        this.blockId = blockId;
    }

    public void createPhoneButton(String buttonName,String phoneNumber){
        this.action = "phone";
        this.label = buttonName;
        this.phoneNumber = phoneNumber;
    }

    public void createMessageButton(String buttonName,String messageText){
        this.action = "message";
        this.label = buttonName;
        this.messageText = messageText;
    }
}