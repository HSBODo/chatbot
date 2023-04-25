package site.pointman.chatbot.dto.kakaoui;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
@Getter
public class KakaoResponseDto {
    private List outputs;

    public KakaoResponseDto() {
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
