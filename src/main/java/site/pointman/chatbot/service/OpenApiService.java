package site.pointman.chatbot.service;



import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.kakaopay.KakaoPay;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReady;
import site.pointman.chatbot.dto.naverapi.Search;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCode;


public interface OpenApiService {
    WeatherPropertyCode selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    Search selectNaverSearch(String searchText, String display,String start, String sort) throws ParseException;

    KakaoPay kakaoPayReady(KakaoPayReady kakaoPayReady) throws Exception;

    KakaoPayReady kakaoPayApprove(String pg_token, String kakaoUserkey) throws Exception;
}
