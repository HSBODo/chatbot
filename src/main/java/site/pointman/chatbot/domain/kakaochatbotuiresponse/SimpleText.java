package site.pointman.chatbot.domain.kakaochatbotuiresponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SimpleText {
    /**
     이름	타입	        필수 여부	설명	제한
     text	string	        O	    전달하고자 하는 텍스트입니다	1000자
     */

    public JSONObject createSimpleText (String msg) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        String resultJson ="{\n" +
                "               \"simpleText\": {\n" +
                "                   \"text\": \""+msg+"\",\n" +
                "               }\n" +
                "           }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
