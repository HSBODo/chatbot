package site.pointman.chatbot.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

//import org.json.JSONObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.domain.KakaoUserLocation;
import site.pointman.chatbot.domain.KakaoUserRequest;
import site.pointman.chatbot.domain.LocationXY;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.BasicCard;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.Button;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.Buttons;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.KakaoResponse;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private WeatherApiService weatherapiservice;

    private KakaoApiService kakaoApiService;

    private KakaoUserLocationRepository kakaoUserLocationRepository;

    public KakaoRestAPI(WeatherApiService weatherapiservice, KakaoApiService kakaoApiService, KakaoUserLocationRepository kakaoUserLocationRepository) {
        this.weatherapiservice = weatherapiservice;
        this.kakaoApiService = kakaoApiService;
        this.kakaoUserLocationRepository = kakaoUserLocationRepository;
    }

    @ResponseBody
    @PostMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoUserRequest params) throws JsonProcessingException, ParseException {
        KakaoResponse response = new KakaoResponse();
        try {
            String uttr = params.getUttr();
            String kakaoUserkey = params.getKakaoUserkey();

            switch (uttr){
                case "위치정보 동의 완료" :
                    KakaoUserLocation kakaoUserLocation = new KakaoUserLocation();
                    kakaoUserLocation.setX("37.4758682");
                    kakaoUserLocation.setY("126.8350464");
                    response.addContent(kakaoApiService.todayWeather(kakaoUserLocation));
                    break;
                case "오늘의 토픽" :
                    break;
                case "오늘의 날씨" :
                    log.info("오늘의 날씨 시작!!");
                    response.addContent(kakaoApiService.locationAgree());
                    break;
                default:
            }

        }catch (Exception e){
            log.info("Exception:::{}",e);
        }
        return response.createKakaoResponse();
    }

    @ResponseBody
    @PostMapping(value = "weatherInfo" , headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> weatherInfo(@RequestBody KakaoUserRequest params){
        HashMap<String,String> resultJson = new HashMap<>();
        try {
            log.info("xy = {} ",params);
            Optional<KakaoUserLocation> userkey = kakaoUserLocationRepository.findByUserkey(params.getKakaoUserkey());
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

    @ResponseBody
    @GetMapping(value = "test" ,headers = {"Accept=application/json; UTF-8"})
    public JSONObject test() throws ParseException {

        KakaoResponse response = new KakaoResponse();
        BasicCard basicCard = new BasicCard();
        Buttons buttonsObj = new Buttons();

        Button button = new Button("webLink","https://e.kakao.com/t/hello-ryan","구경하기");

        buttonsObj.addButton(button);
        List buttons = buttonsObj.createButtons();
        JSONObject basicCardJson=basicCard.createBasicCard("title","description","thumbnailImgUrl",buttons);

        response.addContent(basicCardJson);
        return response.createKakaoResponse();
    }
}
