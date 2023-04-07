package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.domain.KakaoUserLocation;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.KakaoResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KakaoApiService {
    JSONObject todayWeather(KakaoUserLocation kakaoUserLocation) throws  ParseException;
    JSONObject locationAgree() throws ParseException;
    String join(KakaoUser user);
}
