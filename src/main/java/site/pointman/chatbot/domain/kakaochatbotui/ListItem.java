package site.pointman.chatbot.domain.kakaochatbotui;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListItem {
    private List items = new ArrayList<>();
    private JSONParser jsonParser = new JSONParser();

    public void addItem(String title, String description, String imageUrl, String webLink) throws ParseException {
        String item =
                "  {\n" +
                "              \"title\": \""+title+"\",\n" +
                "              \"description\": \""+description+"\",\n" +
                "              \"imageUrl\": \""+imageUrl+"\",\n" +
                "              \"link\": {\n" +
                "                \"web\": \""+webLink+"\"\n" +
                "              }\n" +
                "            }";
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(item);
        this.items.add(resultJsonObj);
    }

    public JSONObject createListItem(String title, List<Map<String,String>> buttons) throws ParseException {
        JSONArray jsonArr = new JSONArray();
        buttons.forEach(button -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action", button.get("action"));
            jsonObj.put("label", button.get("label"));
            jsonObj.put("webLinkUrl",button.get("webLinkUrl"));
            jsonArr.put(jsonObj);
        });
        String resultJson =" {\n" +
                "        \"listCard\": {\n" +
                "          \"header\": {\n" +
                "            \"title\": \""+title+"\"\n" +
                "          },\n" +
                "          \"items\": "+items+",\n" +
                "          \"buttons\": "+jsonArr+"\n" +
                "        }\n" +
                "      }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }

    public JSONObject createCarouselTypeListItem(String title, String description, String thumbnailImgUrl, List<Map<String,String>> buttons) throws ParseException {
        JSONArray jsonArr = new JSONArray();
        buttons.forEach(button -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action", button.get("action"));
            jsonObj.put("label", button.get("label"));
            jsonObj.put("webLinkUrl",button.get("webLinkUrl"));
            jsonArr.put(jsonObj);
        });
        String resultJson =
                "      {\n" +
                "          \"header\": {\n" +
                "            \"title\": \""+title+"\"\n" +
                "          },\n" +
                "          \"items\": "+items+",\n" +
                "          \"buttons\": "+jsonArr+"\n" +
                "      }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
