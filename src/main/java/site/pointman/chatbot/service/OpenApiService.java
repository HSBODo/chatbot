package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReadyDto;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCodeDto;


public interface OpenApiService {
    WeatherPropertyCodeDto selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    SearchDto selectNaverSearch(String searchText, String display, String start, String sort);
    String  kakaoPayReady(Long itemCode, String kakaoUserkey) throws Exception;
    void kakaoPayApprove(String pg_token, Long orderId) throws Exception;
    void kakaoPayCancel(Long orderId) throws Exception;
}
