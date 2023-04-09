package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.util.Map;


public interface WeatherApiService {

    Map<String,String> selectShortTermWeather (KakaoMemberLocation kakaoUserLocation);
    Map<String,String> WeatherCodeFindByName (Map<String,String> param);
}
