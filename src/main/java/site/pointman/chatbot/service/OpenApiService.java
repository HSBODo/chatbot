package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.dto.weatherapi.WeatherPropertyCodeDto;


public interface OpenApiService {
    WeatherPropertyCodeDto selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    SearchDto selectNaverSearch(String searchText, String display, String start, String sort);
    String  kakaoPayReady(Long itemCode,Long optionId,int totalPrice,int quantity,String kakaoUserkey) throws Exception;
    void kakaoPayApprove(String pg_token, Long orderId) throws Exception;
    void kakaoPayCancel(Long orderId) throws Exception;
}
