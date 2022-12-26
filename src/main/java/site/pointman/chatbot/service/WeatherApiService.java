package site.pointman.chatbot.service;



import java.util.Map;


public interface WeatherApiService {

    Map<String,String> selectShortTermWeather (String X ,String Y);
    Map<String,String> WeatherCodeFindByName (Map<String,String> param);
    Map<String,Double> convertGRID_GPS(int mode, double lat_x, double lng_Y);
}
