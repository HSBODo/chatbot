package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;

//import org.json.JSONObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.KakaoRequest;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.*;
import site.pointman.chatbot.repository.KakaoMemberRepository;
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
    private KakaoMemberRepository KakaoMemberRepository;
    private BasicCard basicCard;
    private SimpleImage simpleImage;
    private SimpleText simpleText;


    public KakaoRestAPI(WeatherApiService weatherapiservice, KakaoApiService kakaoApiService, MemberService memberService, KakaoMemberRepository kakaoMemberRepository, BasicCard basicCard, SimpleImage simpleImage, SimpleText simpleText) {
        this.weatherapiservice = weatherapiservice;
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.KakaoMemberRepository = kakaoMemberRepository;
        this.basicCard = basicCard;
        this.simpleImage = simpleImage;
        this.simpleText = simpleText;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequest params) throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        try {
            String uttr = params.getUttr();
            String kakaoUserkey = params.getKakaoUserkey();
            KakaoMember member = new KakaoMember();
            member.setKakaoUserkey(kakaoUserkey);
            member.setPartnerId("pointman");
            memberService.join(member);
            log.info("Request:: uttr ={}, userkey = {}",uttr,kakaoUserkey);
            switch (uttr){
                case "위치정보 동의 완료" :
                    log.info("위치정보 동의 완료!!");
                    KakaoMemberLocation kakaoUserLocation = new KakaoMemberLocation();
                    KakaoMemberLocation location = KakaoMemberRepository.findByLocation(kakaoUserkey).get();
                    kakaoUserLocation.setX(location.getX());
                    kakaoUserLocation.setY(location.getY());
                    kakaoResponse.addContent(kakaoApiService.createTodayWeather(kakaoUserLocation));

                    break;
                case "이미지" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleImage(simpleImage,"보물상자입니다","https://www.pointman.shop/image/location_notice.png"));
                    break;
                case "오늘의 날씨" :
                    kakaoResponse.addContent(kakaoApiService.createLocationNotice(basicCard,kakaoUserkey));
                    break;
                case "심플" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"심플텍스트"));
                    break;
                default:
                    kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"폴백블럭"));
            }

        }catch (Exception e){
            log.info("Exception:::{}",e);
            kakaoResponse.addContent(kakaoApiService.createSimpleText(simpleText,"챗봇에 문제가 생겼습니다. 죄송합니다."));
        }
        log.info("kakaoResponse = {}",kakaoResponse.createKakaoResponse());
        return kakaoResponse.createKakaoResponse();
    }

    @ResponseBody
    @PostMapping(value = "location-agree" , headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> locationAgree(@RequestBody KakaoMemberLocation params){
        HashMap<String,String> resultJson = new HashMap<>();
        try {
            KakaoMember member = new KakaoMember();
            KakaoMemberLocation memberLocation = new KakaoMemberLocation();
            log.info("X={}, Y={}, kakaoUserkey={}",params.getX(),params.getY(),params.getKakaoUserkey());
            member.setKakaoUserkey(params.getKakaoUserkey());
            memberLocation.setKakaoUserkey(params.getKakaoUserkey());
            memberLocation.setX(params.getX());
            memberLocation.setY(params.getY());
            log.info("join={}",memberService.join(member));
            log.info("saveLocation={}",memberService.saveLocation(memberLocation));

            resultJson.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 완료");
        }catch (Exception e){
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
