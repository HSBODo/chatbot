package site.pointman.chatbot.domain.kakaochatbotuiresponse;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class BasicCard {
    public JSONObject createBasicCard(String title, String description, String thumbnailImgUrl, List<Map<String,String>> buttons) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArr = new JSONArray();
        buttons.forEach(button -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action", button.get("action"));
            jsonObj.put("label", button.get("label"));
            jsonObj.put("webLinkUrl",button.get("webLinkUrl"));
            jsonArr.put(jsonObj);
        });
        String resultJson ="{\n" +
                "        \"basicCard\": {\n" +
                "          \"title\": \""+title+"\",\n" +
                "          \"description\": \""+description+"\",\n" +
                "          \"thumbnail\": {\n" +
                "            \"imageUrl\": \""+thumbnailImgUrl+"\"\n" +
                "          },\n" +
                "          \"buttons\": "+jsonArr+"\n" +
                "        }\n" +
                "      }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
