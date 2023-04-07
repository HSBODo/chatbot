package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.KakaoUserLocation;
import site.pointman.chatbot.domain.LocationXY;

import java.util.Map;


public interface WeatherApiService {

    Map<String,String> selectShortTermWeather (KakaoUserLocation kakaoUserLocation);
    Map<String,String> WeatherCodeFindByName (Map<String,String> param);
//    LocationXY convertGRID_GPS(LocationXY xy);
}
