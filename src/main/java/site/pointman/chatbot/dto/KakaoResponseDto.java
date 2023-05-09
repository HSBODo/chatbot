package site.pointman.chatbot.dto;

import lombok.Getter;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;

import java.util.ArrayList;
import java.util.List;

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
    public void addQuickButton(ButtonDto button) {
        this.quickReplies.add(button);
    }
    public JSONObject createKakaoResponse() throws ParseException {
        JSONArray buttons = new JSONArray(this.quickReplies);
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "    \"version\": \"2.0\",\n" +
                "    \"template\": {\n" +
                "        \"outputs\":"+outputs+ ",\n"+
                "        \"quickReplies\":"+buttons+ "\n"+
                "    }\n" +
                "}";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
