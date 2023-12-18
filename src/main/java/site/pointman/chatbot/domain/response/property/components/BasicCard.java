package site.pointman.chatbot.domain.response.property.components;

import lombok.Getter;
import site.pointman.chatbot.domain.response.property.Component;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.Link;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.domain.response.property.common.Thumbnail;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BasicCard extends Component {
    private String title;
    private String description;
    private Thumbnail thumbnail;
    private Profile profile;
    private List<Button> buttons = new ArrayList<>();

    /**
     * social과 profile은 현재 미지원 상태입니다
     *
     * title : 최대 2줄
     * description : 최대 230자
     * thumbnail(필수값)
     * buttons : 최대 3개
     *
     * Information. description 필드
     * 케로셀 타입에서 description은 최대 76자까지 가능합니다.
     * 케로셀의 경우 2줄, 일반 카드의 경우 4줄까지 노출됩니다.
     * 클라이언트에 따라서 230자, 76자보다 적게 노출될 수도 있습니다.
     */

    public void setThumbnail(String imageUrl, Link link, boolean fixedRatio){
        Thumbnail thumbnail = new Thumbnail(imageUrl,link,fixedRatio);
        this.thumbnail = thumbnail;
    }

    public void setThumbnail(String imageUrl, boolean fixedRatio){
        Thumbnail thumbnail = new Thumbnail(imageUrl,fixedRatio);
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

    public void setButtons(List<Button> buttons) {
        if(buttons.size()>3) throw new IllegalArgumentException("BasicCard의 버튼은 최대 3개까지만 추가할 수 있습니다.");

        this.buttons = buttons;
    }

    public void setButton(Button button) {
        if(buttons.size()>3) throw new IllegalArgumentException("BasicCard의 버튼은 최대 3개까지만 추가할 수 있습니다.");

        this.buttons.add(button);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
