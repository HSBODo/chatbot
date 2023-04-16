package site.pointman.chatbot.service;



import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.Search;
import site.pointman.chatbot.dto.wearherapi.WeatherElementCode;


public interface OpenApiService {
    WeatherElementCode selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    Search selectNaverSearch(String searchText, String display,String start, String sort) throws ParseException;

}
