package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public String selectUtter(Map<String,Object> params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
        String utter = userRequest.get("utterance").toString().replace("\n","");
        return utter;
    }

    @Override
    public HashMap<String, Object> createBasicCard(Map<String, String> param) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map test = new HashMap<>();
        test.put("1","1");
        List<HashMap<String,Object>> buttons = createButtons(test);

        HashMap<String, Object> SimpleText = createSimpleText(param);
        Map<String, String> text = objectMapper.convertValue(SimpleText.get("simpleText"), Map.class);

        HashMap<String, Object> basicCard = new HashMap<>();
        HashMap<String, String> imageUrl = new HashMap<>();
        HashMap<String, Object> cardProp = new HashMap<>();

        imageUrl.put("imageUrl","https://cdn.pixabay.com/photo/2022/11/24/02/28/clouds-7613361__340.png");
        cardProp.put("title","오늘의 날씨");
        cardProp.put("description",text.get("text"));
        cardProp.put("thumbnail",imageUrl);

        basicCard.put("buttons",buttons);
        basicCard.put("basicCard",cardProp);

        return basicCard;
    }

    @Override
    public  List<HashMap<String,Object>> createButtons(Map<String, String> param) throws Exception {
        HashMap<String, Object> buttonProp = new HashMap<>();
        List<HashMap<String,Object>> buttons = new ArrayList<>();

        buttonProp.put("label","날씨");
        buttonProp.put("action","webLink");
        buttonProp.put("webLinkUrl","http://54.248.24.34:8080/kkoChat/v1/locationAgree");
        buttons.add(buttonProp);

        logger.info("buttons"+buttons);

        return buttons;
    }

    @Override
    public  HashMap<String, Object>createSimpleText(Map<String, String> param) throws Exception {
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();
        String rtnStr =
                param.get("baseDate")+"\n"+
                "하늘상태: "    + param.get("SKY")+"\n"+
                "기온: "       + param.get("TMP")+"˚C"+"\n"+
                "습도: "       + param.get("REH")+"%"+"\n"+
                "바람: "       + param.get("WSD")+"\n"+
                "강수형태: "    + param.get("PTY")+"\n"+
                "강수확률: "    + param.get("POP")+"%"+"\n"+
                "강수량: "      + param.get("PCP")+"\n"+
                "적설량: "      + param.get("SNO")
                ;

        text.put("text",rtnStr);
        simpleText.put("simpleText",text);
        return simpleText;
    }


}
