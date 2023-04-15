package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.KakaoResponse;
import site.pointman.chatbot.domain.kakaochatbotui.Button;
import site.pointman.chatbot.domain.kakaochatbotui.Buttons;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.KakaoRequest;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.MemberService;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private KakaoApiService kakaoApiService;
    private MemberService memberService;


    public KakaoRestAPI(KakaoApiService kakaoApiService, MemberService memberService) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequest params) throws ParseException {
        KakaoResponse kakaoResponse = new KakaoResponse();
        try {
            String uttr = params.getUttr();
            String kakaoUserkey ="";
            kakaoUserkey = params.getKakaoUserkey();

            KakaoMember member = new KakaoMember();
            member.setKakaoUserkey(kakaoUserkey);
            member.setPartnerId("pointman");
            memberService.join(member);
            log.info("Request:: uttr ={}, userkey = {}",uttr,kakaoUserkey);
            switch (uttr){
                case "위치정보 동의 완료" :

                    kakaoResponse.addContent(kakaoApiService.createTodayWeather(kakaoUserkey));
                    break;
                case "오늘의 날씨" :
                    kakaoResponse.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
                    break;
                case "오늘의 뉴스" :
                    kakaoResponse.addContent(kakaoApiService.createTodayNews("경제"));
                    break;
                case "오늘의 특가 상품" :
                    kakaoResponse.addContent(kakaoApiService.createRecommendItems());
                    break;
                case "오늘의 메뉴" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleText("개발중입니다."));
                    break;
                case "챗 GPT" :
                    kakaoResponse.addContent(kakaoApiService.createSimpleText("개발중입니다."));
                    break;
                case "개발자 소개" :
                    kakaoResponse.addContent(kakaoApiService.createDeveloperInfo());
                    break;
                default:
                    kakaoResponse.addContent(kakaoApiService.createSimpleText("아직 학습이 부족합니다."));
            }

        }catch (Exception e){
            kakaoResponse.addContent(kakaoApiService.createSimpleText("챗봇에 문제가 생겼습니다. 죄송합니다."));
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
}
