package site.pointman.chatbot.domain.kakaochatbotui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpleImage {

    /**
     이름	        타입	        필수 여부	        설명	제한
     imageUrl	    string	        O	            전달하고자 하는 이미지의 url입니다	URL 형식
     altText	    string	        O	            url이 유효하지 않은 경우, 전달되는 텍스트입니다	최대 1000자
     */
    public JSONObject createSimpleImage (String altMsg,String imageUrl) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "               \"simpleImage\": {\n" +
                "                   \"imageUrl\": \""+imageUrl+"\",\n" +
                "                   \"altText\": \""+altMsg+"\",\n" +
                "               }\n" +
                "           }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
