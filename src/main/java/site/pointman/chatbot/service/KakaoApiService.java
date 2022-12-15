package site.pointman.chatbot.service;

import java.util.Map;

public interface KakaoApiService {
    String selectUtter(Map<String,Object> params) throws Exception;
    String RtnStr(Map<String,String> weatherMap) throws  Exception;
}
