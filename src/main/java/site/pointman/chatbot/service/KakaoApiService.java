package site.pointman.chatbot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KakaoApiService {
    String selectUtter(Map<String,Object> params) throws Exception;
    HashMap<String, Object> createSimpleText(Map<String,String> param) throws  Exception;

    HashMap<String, Object> createBasicCard(Map<String,String> param) throws  Exception;
    List<HashMap<String,Object>> createButtons(Map<String,String> param) throws  Exception;

}
