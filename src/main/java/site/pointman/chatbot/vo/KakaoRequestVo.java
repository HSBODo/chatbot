package site.pointman.chatbot.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.util.Map;

@Slf4j
@Getter @Setter  @ToString @NoArgsConstructor
public class KakaoRequestVo {
    private JSONObject intent;
    private JSONObject userRequest;
    private JSONObject bot;
    private JSONObject action;
    private JSONObject buttonParams;
    private String kakaoUserkey;
    private String uttr;
    private String timezone;
    private String partnerId;
    private String blockCode;


    public String getKakaoUserkey() {
        JSONObject user = new JSONObject((Map)userRequest.get("user"));
        JSONObject properties = new JSONObject((Map)user.get("properties"));
        this.kakaoUserkey = (String)properties.get("plusfriendUserKey");
        return kakaoUserkey;
    }

    public String getUttr() {
        this.uttr = (String) userRequest.get("utterance");
        return uttr;
    }

    public String getTimezone() {
        this.timezone = (String) userRequest.get("timezone");
        return timezone;
    }
    public JSONObject getButtonParams() {
        this.buttonParams =  new JSONObject((Map)action.get("clientExtra"));
        return buttonParams;
    }

    public String getBlockCode() {
        getButtonParams();
        this.blockCode = (String) buttonParams.get("blockCode");
        return blockCode;
    }
}
