package site.pointman.chatbot.domain.kakaochatbotui;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class Carousel {
    private List items;
    private String type;

    public Carousel() {
        this.items = new ArrayList<>();
    }

    public void addContent(JSONObject content) {

        this.items.add(content);
    }

    public JSONObject createCarousel() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "    \"carousel\": {\n" +
                "        \"type\": \""+type+"\",\n" +
                "        \"items\":"+items+
                "    }\n" +
                "}";
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
