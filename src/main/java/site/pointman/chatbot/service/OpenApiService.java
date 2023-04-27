package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.vo.naverapi.SearchVo;
import site.pointman.chatbot.vo.weatherapi.WeatherPropertyCodeVo;


public interface OpenApiService {
    WeatherPropertyCodeVo selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    SearchVo selectNaverSearch(String searchText, String display, String start, String sort);
    String  kakaoPayReady(Long itemCode, String kakaoUserkey) throws Exception;
    void kakaoPayApprove(String pg_token, Long orderId) throws Exception;
    void kakaoPayCancel(Long orderId) throws Exception;
}
