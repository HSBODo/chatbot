package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping(value = "/kkoChat/v1/*")
public class KakaoRestAPI {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WeatherApiService weatherapiservice;
    @Autowired
    private KakaoApiService kakaoApiService;
    @ResponseBody
    @RequestMapping(value = "chat" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, Object> callAPI(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,Object> resultJson = new HashMap<>();
        try {
            HashMap<String,Object> template = new HashMap<>();
            List<HashMap<String,Object>> outputs = new ArrayList<>();
            List<HashMap<String,Object>> quikButtons = new ArrayList<>();
            HashMap<String, Object> simpletext;
            logger.info("request"+params);
            String utter = kakaoApiService.selectUtter(params);
            Map<String,String> text = new HashMap<>();
            switch (utter){
                case "테스트" :
                    logger.info("--------------------- 오늘의 날씨 start --------------------");
                    HashMap<String, Object> basicCard;

                    Map<String,Double> latXlngY = weatherapiservice.convertGRID_GPS(0,37.4758682,126.8350464);
                    int x = (int)Math.round(latXlngY.get("X"));
                    int y = (int)Math.round(latXlngY.get("Y"));
                    Map<String,String> weatherCode = weatherapiservice.selectShortTermWeather(Integer.toString(x),Integer.toString(y));
                    basicCard = kakaoApiService.createBasicCard(weatherapiservice.WeatherCodeFindByName(weatherCode));
                    outputs.add(basicCard);
                    logger.info("--------------------- 오늘의 날씨 end --------------------");

                    break;
                case "오늘의 토픽" :
                    text.put("SKY","맑음");
                    simpletext = kakaoApiService.createSimpleText(text);
                    outputs.add(simpletext);
                    break;
                case "오늘의 날씨" :
                    logger.info("--------------------- 오늘의 날씨 start --------------------");
                    text.put("text","현재 위치하신 지역을 선택하세요.");
                    simpletext = kakaoApiService.createSimpleText(text);
                    outputs.add(simpletext);
                    ArrayList labes = new ArrayList<>(Arrays.asList("서울","부산","경기"));
                    quikButtons =kakaoApiService.quickButtons(labes);
                    template.put("quickReplies",quikButtons);
                    logger.info("--------------------- 오늘의 날씨 end --------------------");
                    break;
                default:
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
    @ResponseBody
    @RequestMapping(value = "weatherInfo" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, Object> weatherInfo(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,Object> resultJson = new HashMap<>();
        try {
            HashMap<String,Object> template = new HashMap<>();
            List<HashMap<String,Object>> outputs = new ArrayList<>();
            logger.info("request"+params);
            double latitude = (double)params.get("latitude");
            double longitude = (double)params.get("longitude");
            logger.info("--------------------- 오늘의 날씨 start --------------------");
            Map<String,Double> latXlngY = weatherapiservice.convertGRID_GPS(0,latitude,longitude);
            int x = (int)Math.round(latXlngY.get("X"));
            int y = (int)Math.round(latXlngY.get("Y"));
            Map<String,String> weatherCode = weatherapiservice.selectShortTermWeather(Integer.toString(x),Integer.toString(y));
            HashMap<String, Object> basicCard;
            basicCard = kakaoApiService.createBasicCard(weatherapiservice.WeatherCodeFindByName(weatherCode));
            outputs.add(basicCard);
            logger.info("--------------------- 오늘의 날씨 end --------------------");

            template.put("outputs",outputs);
            resultJson.put("version","2.0");
            resultJson.put("template",template);
        }catch (Exception e){
            System.out.println(e);
        }
        logger.info("final resultJson::: "+resultJson);
        return resultJson;
    }


    @RequestMapping(value = "locationAgree" , method= {RequestMethod.POST,RequestMethod.GET},headers = {"Accept=application/json; UTF-8"})
    public String locationAgree(){
        logger.info("-----------위치정보 동의---------");
        return "locationAgree";
    }




}
