package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class KakaoRestAPI {
    @Autowired
    private WeatherApiService weatherapiservice;
    @Autowired
    private KakaoApiService kakaoApiService;

    @RequestMapping(value = "/kkoChat/v1" , method= {RequestMethod.POST , RequestMethod.GET },headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, Object> callAPI(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,Object> resultJson = new HashMap<>();
        try {
            HashMap<String,Object> template = new HashMap<>();
            List<HashMap<String,Object>> outputs = new ArrayList<>();

            String utter = kakaoApiService.selectUtter(params);
            Map<String,String> weatherCode = weatherapiservice.selectShortTermWeather();
            String rtnStr = "";
            switch (utter){
                case "오늘의 날씨" :

                    HashMap<String, Object> simpleText;
                    simpleText = kakaoApiService.createSimpleText(weatherapiservice.WeatherCodeFindByName(weatherCode));
                    outputs.add(simpleText);
                    break;
                case "기능2" :

                    HashMap<String, Object> basicCard;
                    basicCard = kakaoApiService.createBasicCard(weatherapiservice.WeatherCodeFindByName(weatherCode));
                    outputs.add(basicCard);
                    break;
                case "기능3" : rtnStr = "";
                    break;
                default: rtnStr = "한수빈 개발 챗봇입니다 현재는 개발중입니다";
            }






            template.put("outputs",outputs);
            resultJson.put("version","2.0");
            resultJson.put("template",template);


        }catch (Exception e){
            System.out.println(e);
        }
        return resultJson;
    }




}
