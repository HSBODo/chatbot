package site.pointman.chatbot.service;



import java.util.Map;


public interface WeatherApiService {

    Map<String,String> selectShortTermWeather ();
    Map<String,String> WeatherCodeFindByName (Map<String,String> param);

}
