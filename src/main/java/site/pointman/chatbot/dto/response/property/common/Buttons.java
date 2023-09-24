package site.pointman.chatbot.dto.response.property.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Buttons {
    private List<Button> buttons = new ArrayList<>();

    public void addWebLinkButton(String buttonName,String webLinkUrl){
        Button button = new Button();
        button.createWebLinkButton(buttonName,webLinkUrl);
        buttons.add(button);
    }

    public void addBlockButton(String buttonName,String blockId){
        Button button = new Button();
        button.createBlockButton(buttonName,blockId);
        buttons.add(button);
    }

    public void addPhoneButton(String buttonName,String phoneNumber){
        Button button = new Button();
        button.createPhoneButton(buttonName,phoneNumber);
        buttons.add(button);
    }

    public void addMessageButton(String buttonName,String messageText){
        Button button = new Button();
        button.createMessageButton(buttonName,messageText);
        buttons.add(button);
    }
}
