package site.pointman.chatbot.service;



import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.search.Search;
import site.pointman.chatbot.domain.wearher.WeatherElementCode;

import java.util.Map;


public interface OpenApiService {
    WeatherElementCode selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    Search selectNaverSearch(String searchText, String display,String start, String sort) throws ParseException;

}
