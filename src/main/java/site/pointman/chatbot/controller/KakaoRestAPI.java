package site.pointman.chatbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.dto.KakaoMemberDto;
import site.pointman.chatbot.dto.KakaoMemberLocationDto;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.BlockRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.*;
import site.pointman.chatbot.vo.*;
import site.pointman.chatbot.vo.kakaoui.ButtonType;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ExtraCodeVo;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    private KakaoApiService kakaoApiService;
    private MemberService memberService;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private BlockService blockService;
    private BlockRepository blockRepository;
    private KakaoMemberRepository kakaoMemberRepository;
    private JSONParser jsonParser = new JSONParser();

    public KakaoRestAPI(KakaoApiService kakaoApiService, MemberService memberService, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService, BlockService blockService, BlockRepository blockRepository, KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.blockService = blockService;
        this.blockRepository = blockRepository;
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequestVo request) throws Exception {
        JSONObject response = new JSONObject();
        try {
            String uttr = request.getUttr(); //사용자 발화
            String kakaoUserkey = request.getKakaoUserkey(); //사용자 유저키
            JSONObject buttonParams = request.getButtonParams();
            log.info("Request:: uttr ={}, userkey = {} buttonParams={}",uttr,kakaoUserkey,buttonParams);
            Long blockId = request.getBlockId(); //블럭Id



            BlockDto blockDto = BlockDto.builder()
                    .id(blockId)
                    .blockName("")
                    .blockType(null)
                    .displayType(null)
                    .service(request.getBlockService())
                    .build();
            Optional<KakaoMember> maybeMember = kakaoMemberRepository.findByMember(kakaoUserkey);
            if(maybeMember.isEmpty()) blockDto.setService(BlockServiceType.회원가입);

            Map<String,BlockServiceType> uttrMapping = new HashMap<>();
            uttrMapping.put("오늘의 특가상품",BlockServiceType.상품조회);
            uttrMapping.put("주문조회",BlockServiceType.주문조회);

            if(blockDto.getService().equals(null))blockDto.setService(uttrMapping.get(uttr));


             response = blockService.findByService(kakaoUserkey, blockDto, buttonParams);


//            if(!Optional.ofNullable(request.getBlockId()).isEmpty()){
//                log.info("buttonParams={}",buttonParams);
//                Optional<Block> maybeBlock = blockRepository.findByBlock(blockId);
//                if (maybeBlock.isEmpty()) throw new NullPointerException("블럭이 존재하지 않습니다.");
//                BlockDto blockDto = maybeBlock.get().toBlockDto();
//                JSONObject response = blockService.findByService(kakaoUserkey, blockDto, buttonParams);
//
//                return response;
//            }

//            switch (uttr){
//                case "옵션":
//                    kakaoResponse.addContent(kakaoJsonUiService.createSimpleText("옵션테스트"));

//                    Map<String,String> buttonParam = new HashMap<>();
//                    buttonParam.put("color","red");
//                    ButtonVo button1= new ButtonVo(ButtonType.block,"옵션1",buttonParam);
//                    ButtonVo button2= new ButtonVo(ButtonType.block,"옵션1",buttonParam);
//                    ButtonVo button3= new ButtonVo(ButtonType.block,"옵션1",buttonParam);
//                    ButtonVo button4= new ButtonVo(ButtonType.block,"옵션1",buttonParam);
//                    kakaoResponse.addQuickButton(button1);
//                    kakaoResponse.addQuickButton(button2);
//                    kakaoResponse.addQuickButton(button3);
//                    kakaoResponse.addQuickButton(button4);
//
//                    break;
//                case "결제 상세보기" :
//                    log.info("action={}",request.getAction().get("clientExtra"));
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    ExtraCodeVo extraCodeVo = objectMapper.convertValue(request.getAction().get("clientExtra"), ExtraCodeVo.class);
//                    kakaoResponse.addContent(kakaoApiService.createOrderDetail(kakaoUserkey, extraCodeVo.getOrderId()));
//                    break;
//                case "위치정보 동의 완료" :
//                    kakaoResponse.addContent(kakaoApiService.createTodayWeather(kakaoUserkey));
//                    break;
//                case "오늘의 날씨" :
//                    kakaoResponse.addContent(kakaoApiService.createLocationNotice(kakaoUserkey));
//                    break;
//                case "오늘의 뉴스" :
//                    kakaoResponse.addContent(kakaoApiService.createTodayNews("뉴스"));
//                    break;
//                case "오늘의 특가 상품" :
//                    kakaoResponse.addContent(kakaoApiService.createRecommendItems(kakaoUserkey));
//                    break;
//                case "주문조회" :
//                    kakaoResponse.addContent(kakaoApiService.createOrderList(kakaoUserkey));
//                    break;
//                case "관리자페이지" :
//                    List buttons = new ArrayList<ButtonVo>();
//                    ButtonVo adminPage = new ButtonVo(ButtonType.webLink,"관리자페이지","https://www.pointman.shop/admin-page/login");
//                    buttons.add(adminPage);
//                    kakaoResponse.addContent(kakaoJsonUiService.createBasicCard(DisplayType.basic,"관리자페이지","회원관리, 상품관리, 매출관리를 할 수 있어요!!","https://www.pointman.shop/image/%EC%B6%9C%EA%B7%BC%EB%8F%84%EC%9A%B0%EB%AF%B8.png",buttons));
//                    break;
//                case "개발자 소개" :
//                    kakaoResponse.addContent(kakaoApiService.createDeveloperInfo());
//                    break;
//                default:
//                    kakaoResponse.addContent(kakaoJsonUiService.createSimpleText("아직 학습이 부족합니다."));
//            }
        }catch (Exception e){
            e.printStackTrace();
            KakaoResponseVo kakaoResponse = new KakaoResponseVo();
            kakaoResponse.addContent(kakaoJsonUiService.createSimpleText(e.getMessage()));
            return kakaoResponse.createKakaoResponse();
        }
        log.info("kakaoResponse = {}", response);
        return response;
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
            log.info("pg_token={} orderId={}",pg_token,orderId);
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
