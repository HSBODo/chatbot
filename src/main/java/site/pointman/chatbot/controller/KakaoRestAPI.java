package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;


import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.kakaopay.KakaoPay;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReady;
import site.pointman.chatbot.dto.kakaoui.Button;
import site.pointman.chatbot.dto.kakaoui.KakaoResponse;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.kakaoui.KakaoRequest;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.OpenApiService;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private KakaoApiService kakaoApiService;
    private MemberService memberService;
    private OpenApiService openApiService;

    public KakaoRestAPI(KakaoApiService kakaoApiService, MemberService memberService, OpenApiService openApiService) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.openApiService = openApiService;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequest params) throws Exception {
        KakaoResponse kakaoResponse = new KakaoResponse();
        try {
            log.info("action={}",params.getAction().get("clientExtra"));
            String uttr = params.getUttr();
            String kakaoUserkey;
            kakaoUserkey = params.getKakaoUserkey();

            KakaoMember member = new KakaoMember();
            member.setKakaoUserkey(kakaoUserkey);
            member.setPartnerId("pointman");
            memberService.join(member);

            log.info("Request:: uttr ={}, userkey = {}",uttr,kakaoUserkey);
            switch (uttr){
                case "결제 상세보기" :

                case "위치정보 동의 완료" :
                    kakaoResponse.addContent(kakaoApiService.createTodayWeather(kakaoUserkey));
                    break;
                case "오늘의 날씨" :
                    kakaoResponse.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
                    break;
                case "오늘의 뉴스" :
                    kakaoResponse.addContent(kakaoApiService.createTodayNews("뉴스"));
                    break;
                case "오늘의 특가 상품" :
                    kakaoResponse.addContent(kakaoApiService.createRecommendItems(kakaoUserkey));
                    break;
                case "주문조회" :
                    kakaoResponse.addContent(kakaoApiService.createOrderList(kakaoUserkey));
                    break;
                case "관리자페이지" :
                    List buttonList = new ArrayList<Button>();
                    Button adminPage = new Button("webLink","관리자페이지","https://www.pointman.shop/admin-page/login");
                    buttonList.add(adminPage);
                    kakaoResponse.addContent(kakaoApiService.createBasicCard("","관리자페이지","회원관리, 상품관리, 매출관리를 할 수 있어요!!","https://www.pointman.shop/image/%EC%B6%9C%EA%B7%BC%EB%8F%84%EC%9A%B0%EB%AF%B8.png",buttonList));
                    break;
                case "개발자 소개" :
                    kakaoResponse.addContent(kakaoApiService.createDeveloperInfo());
                    break;
                default:
                    kakaoResponse.addContent(kakaoApiService.createSimpleText("아직 학습이 부족합니다."));
            }

        }catch (Exception e){
            log.info(e.toString());
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

    @GetMapping(value = "kakaopay-ready")
    public String kakaoPayReady(@RequestParam Long itemcode,@RequestParam String kakaouserkey) throws Exception{
        log.info("itemcode={}",itemcode);
        log.info("kakaouserkey={}",kakaouserkey);
        String redirectUrl="https://plus.kakao.com/talk/bot/@pointman_dev/결제실패";
        try {
            KakaoPayReady kakaoPayReady = openApiService.createKakaoPayReady(itemcode, kakaouserkey);
            KakaoPay kakaoPay = openApiService.kakaoPayReady(kakaoPayReady);
            redirectUrl=kakaoPay.getNext_redirect_mobile_url();
            log.info("kakaopay-ready success ={}",redirectUrl);
        }catch (Exception e){
            log.info("e={}",e.toString());
            log.info("kakaopay-ready fail ={}",redirectUrl);
            return "redirect:"+redirectUrl;
        }
        return "redirect:"+redirectUrl;
    }
    @GetMapping(value = "/{orderId}/kakaopay-approve")
    public String kakaoPayApprove (@PathVariable Long orderId, String pg_token) {
        try {
            log.info("pg_token={}",pg_token);
            log.info("orderId={}",orderId);
            openApiService.kakaoPayApprove(pg_token,orderId);
            log.info("kakaopay-approve success");
        }catch (Exception e){
            log.info("kakaopay-approve fail");
            return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/결제 실패";
        }
        return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/결제 완료";
    }
    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }
}
