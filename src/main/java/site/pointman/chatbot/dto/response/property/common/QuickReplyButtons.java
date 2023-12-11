package site.pointman.chatbot.dto.response.property.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QuickReplyButtons {
   private List<QuickReplyButton> quickReplyButtons = new ArrayList<>();

    public void addBlockQuickButton(String buttonName,String blockId,Extra extra){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName,blockId,extra);
        quickReplyButtons.add(quickReplyButton);
    }

    public void addBlockQuickButton(String buttonName,String blockId){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName,blockId);
        quickReplyButtons.add(quickReplyButton);
    }

    public void addBlockQuickButton(String buttonName){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createBlockQuickButton(buttonName);
        quickReplyButtons.add(quickReplyButton);
    }

    public void addMessageQuickButton(String buttonName,String messageText,Extra extra){
        QuickReplyButton quickReplyButton = new QuickReplyButton();
        quickReplyButton.createMessageQuickButton(buttonName,messageText,extra);
        quickReplyButtons.add(quickReplyButton);
    }
}
