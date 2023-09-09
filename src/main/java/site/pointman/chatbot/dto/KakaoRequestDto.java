package site.pointman.chatbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.block.BlockServiceType;

import java.util.Map;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class KakaoRequestDto {
    private JSONObject intent;
    private JSONObject userRequest;
    private JSONObject bot;
    private JSONObject action;
    private JSONObject buttonParams;
    private JSONObject params;
    private String kakaoUserkey;
    private String uttr;
    private String timezone;
    private String partnerId;
    private Long blockId;
    private BlockServiceType blockServiceType;
    private BlockServiceType blockService;

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

    public JSONObject getParams() {
        this.params = new JSONObject((Map)action.get("params"));
        return params;
    }

    public JSONObject getButtonParams() {
        this.buttonParams = new JSONObject((Map)action.get("clientExtra"));
        return buttonParams;
    }
    public Long getBlockId() {
        if(buttonParams.get("blockId")==null) return blockId;
        this.blockId = Long.parseLong((String) buttonParams.get("blockId"));
        return blockId;
    }
    public BlockServiceType getBlockService() {
        if(buttonParams.get("blockService")!=null)  this.blockService =BlockServiceType.valueOf ((String) buttonParams.get("blockService"));
        if(params.get("blockService")!=null)   this.blockService =BlockServiceType.valueOf ((String) params.get("blockService"));
        return blockService;
    }
}