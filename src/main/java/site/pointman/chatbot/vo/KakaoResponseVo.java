package site.pointman.chatbot.vo;

import lombok.Getter;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class KakaoResponseVo {
    private List outputs;
    private List quickReplies;

    public KakaoResponseVo() {
        this.outputs = new ArrayList();
        this.quickReplies = new ArrayList();
    }


    public void addContent(JSONObject content) {
        this.outputs.add(content);
    }
    public void addQuickButton(ButtonVo button) {
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
