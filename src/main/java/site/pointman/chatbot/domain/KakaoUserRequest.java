package site.pointman.chatbot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.util.Map;

@Slf4j
@Getter @Setter
public class KakaoUserRequest {
    private JSONObject intent;
    private JSONObject userRequest;
    private JSONObject bot;
    private JSONObject action;
    private String kakaoUserkey;
    private String uttr;
    private String timezone;

    public KakaoUserRequest(JSONObject intent, JSONObject userRequest, JSONObject bot, JSONObject action) {
        JSONObject user = new JSONObject((Map)userRequest.get("user"));
        JSONObject properties = new JSONObject((Map)user.get("properties"));

        this.timezone = (String) userRequest.get("timezone");
        this.uttr = (String) userRequest.get("utterance");
        this.kakaoUserkey = (String)properties.get("plusfriendUserKey");
        this.intent = intent;
        this.userRequest = userRequest;
        this.bot = bot;
        this.action = action;
    }
}
