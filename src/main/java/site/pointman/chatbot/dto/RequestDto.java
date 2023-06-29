package site.pointman.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.vo.ContextsVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
public class RequestDto {

    private String botId;
    private String botName;
    private String blockId;
    private String blockName;
    private String skillId;
    private String skillName;
    private String utterance;
    private String userKey;
    private String lang;
    private String timezone;
    private String step;
    private String param;

    private String image;
    private ContextsVo contexts;

    @JsonProperty("value")
    private void value(Map<String,Object> value) {
        this.image = (String) value.get("resolved");
    }



    @SuppressWarnings("unchecked")
    @JsonProperty("bot")
    private void bot(Map<String,Object> bot) {
        this.botId = (String)bot.get("id");
        this.botName= (String)bot.get("name");
    }
    @SuppressWarnings("unchecked")
    @JsonProperty("intent")
    private void intent(Map<String,Object> intent) {
        this.blockId = (String)intent.get("id");
        this.blockName = (String)intent.get("name");

    }
    @SuppressWarnings("unchecked")
    @JsonProperty("action")
    private void action(Map<String,Object> action) {
        this.skillId = (String)action.get("id");
        this.skillName = (String)action.get("name");

        Map<String,String> params = (Map<String,String>)action.get("params");
        this.step = params.get("step");

//        Map<String,String> clientExtra = (Map<String,String>)action.get("clientExtra");
//        this.param = clientExtra.get("opt1");

    }
    @SuppressWarnings("unchecked")
    @JsonProperty("userRequest")
    private void userRequest(Map<String,Object> userRequest) {
        this.utterance = (String)userRequest.get("utterance");
        this.lang = (String)userRequest.get("lang");
        this.timezone = (String)userRequest.get("timezone");

        Map<String,Object> user = (Map<String,Object>)userRequest.get("user");
        Map<String,String> properties = (Map<String,String>)user.get("properties");
        this.userKey = properties.get("plusfriendUserKey");


    }
    @SuppressWarnings("unchecked")
    @JsonProperty("contexts")
    private void contexts(ArrayList<ContextsVo> contexts) {
        this.contexts = contexts.get(0);
    }

}
