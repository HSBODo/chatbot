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

    /**
     * 필드명	    타입	            필수 여부	설명	                제한
     * type	        string	            O	케로셀의 타입입니다.	basicCard 혹은 commerceCard, listCard, itemCard
     * items	Array<BasicCard>, Array<CommerceCard>, Array<ListCard>, Array<itemCard>	    O	케로셀 아이템입니다.	최대 10개 *ListCard는 최대 5개
     * header	CarouselHeader	        X	케로셀의 커버를 제공합니다.	*ListCard는 케로셀헤더를 지원하지 않습니다.
     */
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
