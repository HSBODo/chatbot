package site.pointman.chatbot.domain.response.property.components;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.response.property.common.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BasicCard {
    private String title;
    private String description;
    private Thumbnail thumbnail;
    private Profile profile;
    private List<Button> buttons = new ArrayList<>();

    public void setThumbnail(String imageUrl, Link link, boolean fixedRatio){
        Thumbnail thumbnail = new Thumbnail(imageUrl,link,fixedRatio);
        this.thumbnail = thumbnail;
    }

    public void setThumbnail(String imageUrl, Link link){
        Thumbnail thumbnail = new Thumbnail(imageUrl,link);
        this.thumbnail = thumbnail;
    }
    public void setThumbnail(String imageUrl){
        Thumbnail thumbnail = new Thumbnail(imageUrl);
        this.thumbnail = thumbnail;
    }

    public void setProfile(String profileNickname, String profileImgUrl) {
        Profile profile = new Profile(profileNickname,profileImgUrl);
        this.profile = profile;
    }

    public void setBlockButton(String buttonName, String blockId, Extra extra) {
        Button button = new Button();
        button.createBlockButton(buttonName,blockId,extra);
        this.buttons.add(button);
    }

    public void setBlockButton(String buttonName, String blockId) {
        Button button = new Button();
        button.createBlockButton(buttonName,blockId);
        this.buttons.add(button);
    }
    public void setWebLinkButton(String buttonName, String webLink) {
        Button button = new Button();
        button.createWebLinkButton(buttonName,webLink);
        this.buttons.add(button);
    }

    public void setPhoneButton(String buttonName, String phoneNumber) {
        Button button = new Button();
        button.createPhoneButton(buttonName,phoneNumber);
        this.buttons.add(button);
    }

    public void setMessageButton(String buttonName, String message) {
        Button button = new Button();
        button.createMessageButton(buttonName,message);
        this.buttons.add(button);
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
