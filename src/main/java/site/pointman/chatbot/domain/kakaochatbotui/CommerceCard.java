package site.pointman.chatbot.domain.kakaochatbotui;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class CommerceCard  {

    /**
     Information. price, discount, discountedPrice 의 동작 방식

     discountedPrice 가 존재하면 price, discount, discountRate 과 관계 없이 무조건 해당 값이 사용자에게 노출됩니다.
     예) price: 10000, discount: 7000, discountedPrice: 2000 인 경우, 3000 (10000 - 7000)이 아닌 2000이 사용자에게 노출
     위의 예에서 discountedPrice가 없는 경우, 3000이 사용자에게 노출
     예) price: 10000, discountRate: 70, discountedPrice: 2000 인 경우, 3000 (10000 * 0.3)이 아닌 2000이 사용자에게 노출
     discountRate은 discountedPrice를 필요로 합니다. discountedPrice가 주어지지 않으면 사용자에게 >discountRate을 노출하지 않습니다.
     discountRate과 discount가 동시에 있는 경우, discountRate을 우선적으로 노출합니다.

     필드명	            타입	            필수 여부	            설명	                            제한
     description	    string	            O	        제품에 대한 상세 설명입니다.	            최대 40자
     price	            int             	O	        제품의 가격입니다.
     currency	        string	            O	        제품의 가격에 대한 통화입니다.	            현재 won만 가능
     discount	        int	                X	        제품의 가격에 대한 할인할 금액입니다.
     discountRate	    int	                X	        제품의 가격에 대한 할인율입니다.
     dicountedPrice	    int	                X           (discountRate을 쓰는 경우 필수)	        제품의 가격에 대한 할인가(할인된 가격)입니다.
     thumbnails	        Array<Thumbnail>	O	        제품에 대한 사진입니다.	현재 1개만 가능
     profile	        Profile	            X	        제품을 판매하는 프로필 정보입니다.
     buttons	        Array<Button>	    O	        다양한 액션을 수행할 수 있는 버튼입니다.	1개 이상, 3개 이하

     */

    public JSONObject createCommerceCard(String description, int price, int discount, String currency, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String ProfileNickname, List<Map<String,String>> buttons) throws ParseException {
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
                "        \"commerceCard\": {\n" +
                "          \"description\": \""+description+"\",\n" +
                "          \"price\": "+price+",\n" +
                "          \"discount\": "+discount+",\n" +
                "          \"currency\": \""+currency+"\",\n" +
                "          \"thumbnails\": [\n" +
                "            {\n" +
                "              \"imageUrl\": \""+thumbnailImgUrl+"\",\n" +
                "              \"link\": {\n" +
                "                \"web\": \""+thumbnailLink+"\"\n" +
                "              }\n" +
                "            }\n" +
                "          ],\n" +
                "          \"profile\": {\n" +
                "            \"imageUrl\": \""+profileImgUrl+"\",\n" +
                "            \"nickname\": \""+ProfileNickname+"\"\n" +
                "          },\n" +
                "          \"buttons\":"+jsonArr+"\n"+
                "        }\n" +
                "      }";


        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
