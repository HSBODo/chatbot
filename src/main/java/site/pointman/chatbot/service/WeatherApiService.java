package site.pointman.chatbot.service;



import site.pointman.chatbot.domain.LocationXY;

import java.util.Map;


public interface WeatherApiService {

    Map<String,String> selectShortTermWeather (LocationXY xy);
    Map<String,String> WeatherCodeFindByName (Map<String,String> param);
//    LocationXY convertGRID_GPS(LocationXY xy);
}
