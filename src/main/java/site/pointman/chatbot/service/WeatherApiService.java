package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.wearher.WeatherElementCode;



public interface WeatherApiService {
    WeatherElementCode selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
}
