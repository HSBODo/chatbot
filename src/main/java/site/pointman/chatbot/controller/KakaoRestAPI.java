package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.service.serviceimpl.weatherApiServiceImpl;
import site.pointman.chatbot.service.weatherApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class KakaoRestAPI {

    @RequestMapping(value = "/kkoChat/v1" , method= {RequestMethod.POST , RequestMethod.GET },headers = {"Accept=application/json"})
    public HashMap<String, Object> callAPI(@RequestBody Map<String,Object> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,Object> resultJson = new HashMap<>();
        try {
            weatherApiService weatherapiservice = new weatherApiServiceImpl() ;

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(params);
            System.out.println(jsonInString);
            HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
            String utter = userRequest.get("utterance").toString().replace("\n","");
            if(utter=="현재의 날씨"){
                weatherapiservice.selectShortTermWeather();
            }

            System.out.println(  "cont"+ weatherapiservice.selectShortTermWeather());
            String rtnStr = "";
            switch (utter){
                case "고민1" : rtnStr = "챗봇으로 무엇을 만들까?";
                    break;
                case "고민2" : rtnStr = "카페 알바생 너무 이쁘다...";
                    break;
                case "고민3" : rtnStr = "오늘 어디 운동이냐?";
                    break;
                default: rtnStr = "한수빈 개발 챗봇입니다... 현재는 개발중입니다...";
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

        }
        return resultJson;
    }




}
