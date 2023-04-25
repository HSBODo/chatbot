package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface KakaoApiService {

    JSONObject createTodayWeather(String kakaoUserkey) throws Exception;

    JSONObject createTodayNews(String searchText) throws Exception;

    JSONObject createLocationNotice(String kakaoUserkey) throws ParseException;
    JSONObject createDeveloperInfo() throws ParseException;
    JSONObject createOrderDetail(String kakaoUserkey, Long orderId) throws Exception;
    JSONObject createRecommendItems(String kakaoUserkey) throws Exception;
    JSONObject createOrderList(String kakaoUserkey) throws Exception;


}
