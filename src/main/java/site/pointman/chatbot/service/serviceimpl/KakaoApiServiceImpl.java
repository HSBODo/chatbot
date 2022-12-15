package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.KakaoApiService;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    @Override
    public String selectUtter(Map<String,Object> params) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(params);
        System.out.println(jsonInString);
        HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
        String utter = userRequest.get("utterance").toString().replace("\n","");
        return utter;
    }

    @Override
    public String RtnStr(Map<String, String> weatherMap) throws Exception {

        String rtnStr = "";
        return null;
    }


}
