package site.pointman.chatbot.service;



import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;




public interface weatherApiService {
    @Autowired
    JSONObject selectShortTermWeather ();

}
