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

    @Autowired
    private WeatherApiService weatherapiservice;

    @Override
    public String selectUtter(Map<String,Object> params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
        String utter = userRequest.get("utterance").toString().replace("\n","");
        return utter;
    }

    @Override
    public HashMap<String, Object> createBasicCard(Map<String, String> weatherMap) throws Exception {
        HashMap<String, Object> basicCard = new HashMap<>();
        HashMap<String, String> imageUrl = new HashMap<>();
        HashMap<String, Object> cardProp = new HashMap<>();

        imageUrl.put("imageUrl","sdasdasdasdas");
        cardProp.put("title","오늘의 날씨");
        cardProp.put("description","테스트");
        cardProp.put("thumbnail",imageUrl);
        basicCard.put("basicCard",cardProp);


        return basicCard;
    }

    @Override
    public HashMap<String, Object> createButtons(Map<String, String> param) throws Exception {
        HashMap<String, Object> buttons = new HashMap<>();
        HashMap<String, String> buttonProp = new HashMap<>();

        buttonProp.put("label","테스트");
        buttonProp.put("action","테스트");
        buttonProp.put("messageText","테스트");

        buttons.put("buttons",buttonProp);
        return null;
    }

    @Override
    public  HashMap<String, Object>createSimpleText(Map<String, String> weatherCode) throws Exception {
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();
        String rtnStr =
                weatherCode.get("baseDate")+"오늘의 날씨"+"\n"+
                "하늘상태: "    + weatherCode.get("SKY")+"\n"+
                "기온: "       + weatherCode.get("TMP")+"˚C"+"\n"+
                "습도: "       + weatherCode.get("REH")+"%"+"\n"+
                "바람: "       + weatherCode.get("WSD")+"\n"+
                "강수형태: "    + weatherCode.get("PTY")+"\n"+
                "강수확률: "    + weatherCode.get("POP")+"%"+"\n"+
                "강수량: "      + weatherCode.get("PCP")+"\n"+
                "적설량: "      + weatherCode.get("SNO")
                ;

        text.put("text",rtnStr);
        simpleText.put("simpleText",text);
        return simpleText;
    }


}
