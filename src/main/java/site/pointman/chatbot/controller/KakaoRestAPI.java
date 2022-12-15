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
            String utter = kakaoApiService.selectUtter(params);
            System.out.println("utter="+utter);
            String rtnStr = "";
            switch (utter){
                case "현재의 날씨" :

                    System.out.println("CON"+      weatherapiservice.selectShortTermWeather());

                    rtnStr = "오늘날씨";
                    break;
                case "기능2" : rtnStr = "오늘토픽뉴스";
                    break;
                case "기능3" : rtnStr = "";
                    break;
                default: rtnStr = "한수빈 개발 챗봇입니다 현재는 개발중입니다";
            }

            List<HashMap<String,Object>> outputs = new ArrayList<>();
            HashMap<String,Object> template = new HashMap<>();
            HashMap<String, Object> simpleText = new HashMap<>();
            HashMap<String, Object> text = new HashMap<>();

            text.put("text",rtnStr);
            simpleText.put("simpleText",text);
            outputs.add(simpleText);

            template.put("outputs",outputs);

            resultJson.put("version","2.0");
            resultJson.put("template",template);


        }catch (Exception e){
            System.out.println(e);
        }
        return resultJson;
    }




}
