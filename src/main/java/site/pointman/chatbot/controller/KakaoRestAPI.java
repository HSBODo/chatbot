package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.dto.KakaoMemberDto;
import site.pointman.chatbot.dto.KakaoMemberLocationDto;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.OpenApiService;
import site.pointman.chatbot.vo.*;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ExtraCodeVo;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private KakaoApiService kakaoApiService;
    private MemberService memberService;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private JSONParser jsonParser = new JSONParser();

    public KakaoRestAPI(KakaoApiService kakaoApiService, MemberService memberService, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequestVo params) throws Exception {
        KakaoResponseVo kakaoResponseVo = new KakaoResponseVo();
        try {
            String uttr = params.getUttr(); //사용자 발화
            String kakaoUserkey = params.getKakaoUserkey(); //사용자 유저키

            log.info("Request:: uttr ={}, userkey = {}",uttr,kakaoUserkey);
            KakaoMemberDto kakaoMemberDto = KakaoMemberDto.builder()
                    .kakaoUserkey(kakaoUserkey)
                    .partnerId("pointman")
                    .build();
            KakaoMember member = kakaoMemberDto.toEntity();
            memberService.join(member); //<==중복회원 체크 및 회원가입

            switch (uttr){
                case "결제 상세보기" :
                    log.info("action={}",params.getAction().get("clientExtra"));
                    ObjectMapper objectMapper = new ObjectMapper();
                    ExtraCodeVo extraCodeVo = objectMapper.convertValue(params.getAction().get("clientExtra"), ExtraCodeVo.class);

                    kakaoResponseVo.addContent(kakaoApiService.createOrderDetail(kakaoUserkey, extraCodeVo.getOrderId()));
                    break;
                case "위치정보 동의 완료" :
                    kakaoResponseVo.addContent(kakaoApiService.createTodayWeather(kakaoUserkey));
                    break;
                case "오늘의 날씨" :
                    kakaoResponseVo.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
                    break;
                case "오늘의 뉴스" :
                    kakaoResponseVo.addContent(kakaoApiService.createTodayNews("뉴스"));
                    break;
                case "오늘의 특가 상품" :
                    kakaoResponseVo.addContent(kakaoApiService.createRecommendItems(kakaoUserkey));
                    break;
                case "주문조회" :
                    kakaoResponseVo.addContent(kakaoApiService.createOrderList(kakaoUserkey));
                    break;
                case "관리자페이지" :
                    List buttonList = new ArrayList<ButtonVo>();
                    ButtonVo adminPage = new ButtonVo("webLink","관리자페이지","https://www.pointman.shop/admin-page/login");
                    buttonList.add(adminPage);
                    kakaoResponseVo.addContent(kakaoJsonUiService.createBasicCard(DisplayType.basic,"관리자페이지","회원관리, 상품관리, 매출관리를 할 수 있어요!!","https://www.pointman.shop/image/%EC%B6%9C%EA%B7%BC%EB%8F%84%EC%9A%B0%EB%AF%B8.png",buttonList));
                    break;
                case "개발자 소개" :
                    kakaoResponseVo.addContent(kakaoApiService.createDeveloperInfo());
                    break;
                default:
                    kakaoResponseVo.addContent(kakaoJsonUiService.createSimpleText("아직 학습이 부족합니다."));
            }

        }catch (Exception e){
            e.printStackTrace();
            kakaoResponseVo.addContent(kakaoJsonUiService.createSimpleText(e.getMessage()));
        }
        log.info("kakaoResponse = {}", kakaoResponseVo.createKakaoResponse());
        return kakaoResponseVo.createKakaoResponse();
    }

    @ResponseBody
    @PostMapping(value = "location-agree" , headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> locationAgree(@RequestBody KakaoMemberLocationDto kakaoMemberLocationDto){
        HashMap<String,String> redirectURL = new HashMap<>();
        try {

            KakaoMemberDto kakaoMemberDto = KakaoMemberDto.builder()
                    .kakaoUserkey(kakaoMemberLocationDto.getKakaoUserkey())
                    .build();
            KakaoMember member = kakaoMemberDto.toEntity();
            memberService.join(member); //<==회원 체크 및 회원 가입

            KakaoMemberLocation kakaoMemberLocation = kakaoMemberLocationDto.toEntity();
            memberService.saveLocation(kakaoMemberLocation); //<== 위치정보 저장 및 업데이트
            redirectURL.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 완료");
        }catch (Exception e){
            e.printStackTrace();
            redirectURL.put("redirectURL","https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 실패");
        }
        return redirectURL;
    }

    @GetMapping(value = "kakaopay-ready")
    public String kakaoPayReady(@RequestParam Long itemcode,@RequestParam String kakaouserkey) throws Exception{
        log.info("itemcode={},kakaouserkey={}",itemcode,kakaouserkey);
        String redirectUrl;
        try {
            redirectUrl = openApiService.kakaoPayReady(itemcode, kakaouserkey);
            log.info("kakaopay-ready success ={}",redirectUrl);
        }catch (Exception e){
            e.printStackTrace();
            redirectUrl="https://plus.kakao.com/talk/bot/@pointman_dev/결제실패";
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
            e.printStackTrace();
            log.info("kakaopay-approve fail");
            return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/결제실패";
        }
        return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/결제완료";
    }

    @GetMapping(value = "/{orderId}/kakaopay-cancel")
    public String kakaoPayCancel (@PathVariable Long orderId) {
        try {
            openApiService.kakaoPayCancel(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/주문취소실패";
        }
        return "redirect:https://plus.kakao.com/talk/bot/@pointman_dev/주문취소완료";
    }
    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }
}
