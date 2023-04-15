package site.pointman.chatbot.domain.kakaochatbotui;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class BasicCard {
    /**
     Information. description 필드

     케로셀 타입에서 description은 최대 76자까지 가능합니다.
     케로셀의 경우 2줄, 일반 카드의 경우 4줄까지 노출됩니다.
     클라이언트에 따라서 230자, 76자보다 적게 노출될 수도 있습니다.

     필드명	        타입	        필수 여부	    설명	제한
     title	        string	        X	        카드의 제목입니다.	최대 2줄
     description	string	        X	        카드에 대한 상세 설명입니다.	최대 230자
     thumbnail	    Thumbnail	    O	        카드의 상단 이미지입니다.
     profile	    Profile	        X	        카드의 프로필 정보입니다.
     social	        Social	        X	        카드의 소셜 정보입니다.
     buttons	    Array<Button>	X	        카드의 버튼들을 포함합니다.	최대 3개
     * social과 profile은 현재 미지원 상태입니다.

     */
    private JSONParser jsonParser = new JSONParser();
    public JSONObject createBasicCard(String title, String description, String thumbnailImgUrl, List<Map<String,String>> buttons) throws ParseException {
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

    public JSONObject createCarouselTypeBasicCard(String title, String description, String thumbnailImgUrl, List<Map<String,String>> buttons) throws ParseException {
        JSONArray jsonArr = new JSONArray();

        buttons.forEach(button -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action", button.get("action"));
            jsonObj.put("label", button.get("label"));
            jsonObj.put("webLinkUrl",button.get("webLinkUrl"));
            jsonArr.put(jsonObj);
        });
        String resultJson ="{\n" +
                "          \"title\": \""+title+"\",\n" +
                "          \"description\": \""+description+"\",\n" +
                "          \"thumbnail\": {\n" +
                "            \"imageUrl\": \""+thumbnailImgUrl+"\"\n" +
                "          },\n" +
                "          \"buttons\": "+jsonArr+"\n" +
                "      }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
