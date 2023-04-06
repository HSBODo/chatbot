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
    @RequestMapping(value = "chat" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoUserRequest params) throws JsonProcessingException, ParseException {
        KakaoResponse response = new KakaoResponse();
        try {
//            KakaoUserRequest userProps = getStringObjectMap(params);
//            log.info("params ={}",userProps);
//            String join = kakaoApiService.join(user);
//            log.info("join= {}",join);



            String test ="오늘의 날씨";

            Map<String,String> text = new HashMap<>();
            switch (test){
                case "위치정보 동의 완료" :
                    break;
                case "오늘의 토픽" :
                    break;
                case "오늘의 날씨" :
                    log.info("오늘의 날씨 시작!!");
                    BasicCard basicCard = new BasicCard();
                    Buttons buttons = new Buttons();
                    Button button = new Button("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/locationAgree");
                    buttons.addButton(button);
                    JSONObject basicCardJson=basicCard.createBasicCard(
                            "위치정보의 수집ㆍ이용"," ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                            " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                            " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                            "https://www.pointman.shop/image/location_notice.png",
                            buttons.createButtons()
                    );
                    response.addContent(basicCardJson);
                    break;
                default:
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return response.createKakaoResponse();
    }

    @ResponseBody
    @RequestMapping(value = "weatherInfo" , method= {RequestMethod.POST},headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> weatherInfo(@RequestBody Map<String,String> params, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,String> resultJson = new HashMap<>();
        try {
            log.info("xy = {} ",params);
            Optional<KakaoUserLocation> userkey = kakaoUserLocationRepository.findByUserkey(params.get("userkey"));



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

    private static Map<String, Object> getMapByJsonObject(JSONObject jsonObj){
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
//    private static KakaoUserRequest getStringObjectMap(KakaoUserRequest params) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        JSONObject userReqObject = new JSONObject(params.getUserRequest());
//        Map<String, Object> userRequestMap = getMapByJsonObject(userReqObject);
//        String userJson = objectMapper.writeValueAsString(userRequestMap.get("user"));
//
//        JSONObject userObject = new JSONObject(userJson);
//        Map<String, Object> userMap = getMapByJsonObject(userObject);
//        String userpropertiesJson = objectMapper.writeValueAsString(userMap.get("properties"));
//
//        JSONObject userPropObject = new JSONObject(userpropertiesJson);
//        Map<String, Object> propsMap = getMapByJsonObject(userPropObject);
//
//        String kakaoUserkey= (String) propsMap.get("plusfriendUserKey");
//        String utter = (String) userReqObject.get("utterance");
//
//        params.setUttr(utter);
//        params.setKakaoUserkey(kakaoUserkey);
//
//        return params;
//    }



}
