package site.pointman.chatbot.dto;

import lombok.Getter;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import site.pointman.chatbot.dto.kakaoui.ButtonAction;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class KakaoResponseDto {
    private List outputs;
    private List quickReplies;

    public KakaoResponseDto() {
        this.outputs = new ArrayList();
        this.quickReplies = new ArrayList();
    }


    public void addContent(JSONObject content) {
        this.outputs.add(content);
    }

    public void addQuickButton(ButtonAction action,String label, String blockId) {
        ButtonDto quickButton = new ButtonDto(action,label,blockId);
        this.quickReplies.add(quickButton);
    }
    public void addQuickButton(ButtonAction action,String label, Map<String,String> params) {
        ButtonDto quickButton = new ButtonDto(action,label,params);
        this.quickReplies.add(quickButton);
    }
    public JSONObject createKakaoResponse() throws Exception {
        if(this.outputs.equals(null)){
            throw new Exception("outputs 컨텐츠가 반드시 필요합니다.");
        }
        JSONArray buttons = new JSONArray(this.quickReplies);
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "    \"version\": \"2.0\",\n" +
                "    \"template\": {\n" +
                "        \"outputs\":"+outputs+ ",\n"+
                "        \"quickReplies\":"+buttons+ "\n"+
                "    }\n" +
                ""+
                "}";
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
