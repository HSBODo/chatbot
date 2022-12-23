package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(getClass());
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

            String rtnStr = "";
            switch (utter){
                case "오늘의 날씨" :
                    logger.info("--------------------- 오늘의 날씨 start --------------------");
                    Map<String,String> weatherCode = weatherapiservice.selectShortTermWeather();
                    HashMap<String, Object> basicCard;
                    basicCard = kakaoApiService.createBasicCard(weatherapiservice.WeatherCodeFindByName(weatherCode));
                    outputs.add(basicCard);
                    logger.info("--------------------- 오늘의 날씨 end --------------------");
                    break;
                case "오늘의 토픽" :

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
        logger.info("final resultJson::: "+resultJson);
        return resultJson;
    }




}
