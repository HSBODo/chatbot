package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;

//import org.json.JSONObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.domain.KakaoUserRequest;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.*;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private WeatherApiService weatherapiservice;

    private KakaoApiService kakaoApiService;
    private MemberService memberService;
    private KakaoUserLocationRepository kakaoUserLocationRepository;
    private BasicCard basicCard;
    private SimpleImage simpleImage;
    private SimpleText simpleText;


    public KakaoRestAPI(WeatherApiService weatherapiservice, KakaoApiService kakaoApiService, MemberService memberService, KakaoUserLocationRepository kakaoUserLocationRepository, BasicCard basicCard, SimpleImage simpleImage, SimpleText simpleText) {
        this.weatherapiservice = weatherapiservice;
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.kakaoUserLocationRepository = kakaoUserLocationRepository;
        this.basicCard = basicCard;
        this.simpleImage = simpleImage;
        this.simpleText = simpleText;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoUserRequest params) throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        try {
            String uttr = params.getUttr();
            String kakaoUserkey = params.getKakaoUserkey();
            log.info("Request:: uttr ={}, userkey = {}",uttr,kakaoUserkey);
            switch (uttr){
                case "위치정보 동의 완료" :
                    log.info("위치정보 동의 완료!!");
                    KakaoMemberLocation kakaoUserLocation = new KakaoMemberLocation();
                    KakaoMemberLocation location = kakaoUserLocationRepository.findByLocation(kakaoUserkey).get();
                    kakaoUserLocation.setX(location.getX());
                    kakaoUserLocation.setY(location.getY());
                    kakaoResponse.addContent(kakaoApiService.todayWeather(kakaoUserLocation));
                    break;
                case "이미지" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleImage(simpleImage,"보물상자입니다","https://www.pointman.shop/image/location_notice.png"));
                    break;
                case "오늘의 날씨" :
                    Buttons buttons = new Buttons();
                    log.info("오늘의 날씨 시작!!");
                    Button button = new Button("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
                    buttons.addButton(button);
                    kakaoResponse.addContent(kakaoApiService.createBasicCard(
                            basicCard,
                            "위치정보의 수집ㆍ이용",
                            " ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                            " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                            " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                            "https://www.pointman.shop/image/location_notice.png",
                            buttons
                            ));
                    break;
                case "심플" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"심플텍스트"));
                    break;
                default:
                    kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"폴백블럭"));
            }

        }catch (Exception e){
            log.info("Exception:::{}",e.toString());
            kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"챗봇에 문제가 생겼습니다. 죄송합니다."));
        }
        return kakaoResponse.createKakaoResponse();
    }

    @ResponseBody
    @PostMapping(value = "location-agree" , headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> locationAgree(@RequestBody KakaoMemberLocation params){
        HashMap<String,String> resultJson = new HashMap<>();
        try {
            KakaoMemberLocation userLocation = new KakaoMemberLocation();
            log.info("X={}",params.getX());
            log.info("Y={}",params.getY());
            log.info("kakaoUserkey={}",params.getKakaoUserkey());
            userLocation.setKakaoUserkey(params.getKakaoUserkey());
            userLocation.setX(params.getX());
            userLocation.setY(params.getY());
            log.info("join={}",memberService.join(userLocation));
            log.info("saveLocation={}",memberService.saveLocation(userLocation));



            resultJson.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 완료");
        }catch (Exception e){
            log.info("Exception={}",e);
            resultJson.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 실패");
        }
        return resultJson;
    }


    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }

    @ResponseBody
    @GetMapping(value = "test" ,headers = {"Accept=application/json; UTF-8"})
    public JSONObject test() throws ParseException {

        KakaoResponse response = new KakaoResponse();
        BasicCard basicCard = new BasicCard();
        Buttons buttonsObj = new Buttons();
        CommerceCard commerceCard =new CommerceCard();
        Button button = new Button("webLink","https://e.kakao.com/t/hello-ryan","구경하기");

        buttonsObj.addButton(button);
        List buttons = buttonsObj.createButtons();
        JSONObject basicCardJson=commerceCard.createCommerceCard("asda",10000,1000,"123","title","description","thumbnailImgUrl","",buttons);

        response.addContent(basicCardJson);
        return response.createKakaoResponse();
    }
}
