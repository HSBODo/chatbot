package site.pointman.chatbot.domain;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
@Getter
public class KakaoResponse {
    private List outputs;

    public KakaoResponse() {
        this.outputs = new ArrayList();
    }
    public void addContent(JSONObject content) {

        this.outputs.add(content);
    }
    public JSONObject createKakaoResponse() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "    \"version\": \"2.0\",\n" +
                "    \"template\": {\n" +
                "        \"outputs\":"+outputs+
                "    }\n" +
                "}";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
