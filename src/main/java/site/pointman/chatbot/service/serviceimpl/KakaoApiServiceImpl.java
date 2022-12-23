package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {

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

        HashMap<String, String> buttons = createButtons(param);

        HashMap<String, Object> SimpleText = createSimpleText(param);
        Map<String, String> text = objectMapper.convertValue(SimpleText.get("simpleText"), Map.class);

        HashMap<String, Object> basicCard = new HashMap<>();
        HashMap<String, String> imageUrl = new HashMap<>();
        HashMap<String, Object> cardProp = new HashMap<>();

        imageUrl.put("imageUrl","https://cdn.pixabay.com/photo/2022/11/24/02/28/clouds-7613361__340.png");
        cardProp.put("title","오늘의 날씨");
        cardProp.put("description",text.get("text"));
        cardProp.put("thumbnail",imageUrl);
       // basicCard.put("buttons",buttons);
        basicCard.put("basicCard",cardProp);

        return basicCard;
    }

    @Override
    public HashMap<String, String> createButtons(Map<String, String> param) throws Exception {
        HashMap<String, String> buttonProp = new HashMap<>();

        buttonProp.put("label","테스트");
        buttonProp.put("action","테스트");
        buttonProp.put("messageText","테스트");
        return buttonProp;
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
