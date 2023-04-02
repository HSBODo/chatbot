package site.pointman.chatbot.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.KakaoUserRequest;
import site.pointman.chatbot.domain.LocationXY;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kkoChat/v1/*")
public class KakaoRestAPI {
    //Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WeatherApiService weatherapiservice;
    @Autowired
    private KakaoApiService kakaoApiService;


    @ResponseBody
    @RequestMapping(value = "chat" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, Object> callAPI(@RequestBody KakaoUserRequest params, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        HashMap<String,Object> resultJson = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(params.getUserRequest());
            Map<String, Object> userRequest = getMapFromJsonObject(jsonObject);
            log.info("uttr= {}",userRequest.get("utterance"));


            HashMap<String,Object> template = new HashMap<>();
            List<HashMap<String,Object>> outputs = new ArrayList<>();
            List<HashMap<String,Object>> quikButtons = new ArrayList<>();
            HashMap<String, Object> simpletext;
            String utter = (String) userRequest.get("utterance");


            Map<String,String> text = new HashMap<>();
            switch (utter){
                case "위치정보 동의 완료" :
                    log.info("--------------------- 오늘의 날씨 start --------------------");
                    HashMap<String, Object> basicCard;
                    LocationXY xy = new LocationXY("37.4758682","126.8350464",0);
                    Map<String,String> weatherCode = weatherapiservice.selectShortTermWeather(xy);
                    basicCard = kakaoApiService.createBasicCard(weatherapiservice.WeatherCodeFindByName(weatherCode));
                    outputs.add(basicCard);
                    log.info("--------------------- 오늘의 날씨 end --------------------");
                    break;
                case "오늘의 토픽" :
                    text.put("SKY","맑음");
                    simpletext = kakaoApiService.createSimpleText(text);
                    outputs.add(simpletext);
                    break;
                case "오늘의 날씨" :
                    log.info("--------------------- 오늘의 날씨 start --------------------");
                    Map<String, String> param = new HashMap<>();
                    param.put("label","위치정보 동의");
                    param.put("action","webLink");
                    param.put("webLinkUrl","http://localhost:8080/kkoChat/v1/locationAgree");
                    param.put("text","");
                    param.put("title","위치정보제공");
                    basicCard = kakaoApiService.createBasicCard(param);
                    outputs.add(basicCard);
                    log.info("--------------------- 오늘의 날씨 end --------------------");
                    break;
                default:
            }

            template.put("outputs",outputs);
            resultJson.put("version","2.0");
            resultJson.put("template",template);
        }catch (Exception e){
            System.out.println(e);
        }
        log.info("final resultJson::: "+resultJson);
        return resultJson;
    }
    @ResponseBody
    @RequestMapping(value = "weatherInfo" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> weatherInfo(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,String> resultJson = new HashMap<>();
        try {
            log.info("xy = {} ",params);
            resultJson.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 완료");
        }catch (Exception e){
            System.out.println(e);
        }
        return resultJson;
    }


    @GetMapping(value = "locationAgree" ,headers = {"Accept=application/json; UTF-8"})
    public String locationAgree(){
        log.info("-----------위치정보 동의---------");
        return "locationAgree";
    }

    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj){
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
        } catch (JsonParseException e) {

            e.printStackTrace();
        } catch (JsonMappingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return map;
    }



}
