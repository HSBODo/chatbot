package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReady;
import site.pointman.chatbot.dto.naverapi.Search;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCode;


public interface OpenApiService {
    WeatherPropertyCode selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    Search selectNaverSearch(String searchText, String display,String start, String sort);

    KakaoPayReady createKakaoPayReady(Long itemCode,String kakaoUserkey)throws Exception;


    Order kakaoPayReady(KakaoPayReady kakaoPayReady) throws Exception;

    KakaoPayReady kakaoPayApprove(String pg_token, Long orderId) throws Exception;
}
