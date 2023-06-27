package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.Platform;
import site.pointman.chatbot.domain.member.RoleType;
import site.pointman.chatbot.dto.*;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.address.AddressDto;
import site.pointman.chatbot.dto.block.BlockDto;
import site.pointman.chatbot.dto.member.MemberLocationDto;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.repository.AddressRepository;
import site.pointman.chatbot.service.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {

    private MemberService memberService;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private BlockService blockService;
    private AddressRepository addressRepository;

    public KakaoRestAPI(MemberService memberService, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService, BlockService blockService, AddressRepository addressRepository) {
        this.memberService = memberService;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.blockService = blockService;
        this.addressRepository = addressRepository;
    }
    @Value("${kakao.channel.url}")
    private String kakaoChannelUrl;
    private JSONParser jsonParser = new JSONParser();

    @ResponseBody
    @RequestMapping(value = "test" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject connectionTest(@RequestBody String request) throws Exception {

            log.info("request::={}",request);
            KakaoResponseDto kakaoResponse = new KakaoResponseDto();
            return kakaoResponse.createKakaoResponse();

    }



    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequestDto request) throws Exception {
        JSONObject response;
        try {
            String uttr = request.getUttr(); //사용자 발화
            String kakaoUserkey = request.getKakaoUserkey(); //사용자 유저키
            JSONObject buttonParams = request.getButtonParams(); //버튼 파라미터
            JSONObject params = request.getParams(); //블럭 파라미터
            BlockServiceType service = request.getBlockService(); //블럭 서비스

            log.info("Request:: uttr ={}, userkey = {} buttonParams={} params={}",uttr,kakaoUserkey,buttonParams,params);


            if(!memberService.isKakaoMember(kakaoUserkey)) service=BlockServiceType.회원가입;
            if(service==null) {
                Map<String, BlockServiceType> uttrToBlockService = new HashMap<>();
                uttrToBlockService.put("배송지등록완료", BlockServiceType.주문서);
                BlockServiceType blockServiceType = uttrToBlockService.get(uttr);
                service=blockServiceType;
            }
            log.info("service={}",service);
            if(service==null) return null; //<== 버튼 파라미터 서비스가 존재하지 않고 발화 서비스가 존재하지않으면 응답 없음
            memberService.saveAttribute(buttonParams,kakaoUserkey); //<==선택한 버튼 파라미터 저장

            response = blockService.chatBotController(kakaoUserkey, service, buttonParams);
            log.info("kakaoResponse = {}", response);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            KakaoResponseDto kakaoResponse = new KakaoResponseDto();
            kakaoResponse.addContent(kakaoJsonUiService.createSimpleText(e.getMessage()));
            return kakaoResponse.createKakaoResponse();
        }
    }

    @GetMapping(value = "address")
    public String addressForm(@ModelAttribute AddressDto addressDto){
        return "address/addForm";
    }

    @PostMapping(value = "address")
    public String addAddress(@Validated @ModelAttribute AddressDto addressDto, BindingResult bindingResult)throws Exception{
        String result;
        if(bindingResult.hasErrors()){//  addressDto 유효성 검증
            return "address/addForm";
        }
        try {
            Optional<Address> maybeAddress = addressRepository.findByAddress(addressDto.getKakaoUserkey());
            if(maybeAddress.isEmpty()){
                Address address = addressDto.toEntity();
                addressRepository.save(address);
            }else {
                Address address = addressDto.toEntity();
                addressRepository.update(address);
            }
        }catch (Exception e){
            e.printStackTrace();
            result= URLEncoder.encode("배송지등록실패","UTF-8");
            return "redirect:"+kakaoChannelUrl+"/"+result;
        }
        result= URLEncoder.encode("배송지등록완료","UTF-8");
        return "redirect:"+kakaoChannelUrl+"/"+result;
    }

    @GetMapping(value = "kakaoJoinForm")
    public String kakaoMemberJoinForm(@ModelAttribute MemberDto memberDto){
        return "member/kakaoJoinForm";
    }
    @PostMapping(value = "kakao-member")
    public String kakaoMemberJoin(@Validated @ModelAttribute MemberDto memberDto, BindingResult bindingResult)throws Exception{
        String result;
        if(bindingResult.hasErrors()){//  addressDto 유효성 검증
            return "member/kakaoJoinForm";
        }
        try {
            if(memberDto.getKakaoUserkey()=="") throw new NullPointerException("유저키가 없습니다.");
            memberDto.setPlatform(Platform.KAKAO);
            memberDto.setRoleType(RoleType.MEMBER);
            memberService.join(memberDto);
            result= URLEncoder.encode("회원가입성공","UTF-8");
            return "redirect:"+kakaoChannelUrl+"/"+result;
        }catch (Exception e){
            e.printStackTrace();
            result= URLEncoder.encode("회원가입실패","UTF-8");
            return "redirect:"+kakaoChannelUrl+"/"+result;
        }
    }
    @GetMapping(value = "kakaoMemeberDeleteForm")
    public String kakaoMemberDeleteForm(){
        return "member/kakaoDeleteForm";
    }

    @ResponseBody
    @DeleteMapping(value = "kakao-member/{kakaoUserkey}")
    public HashMap<String, String> kakaoMemberDelete(@PathVariable String kakaoUserkey)throws Exception{
        HashMap<String,String> redirectURL = new HashMap<>();
        String result;
        String msg;
        try {
            if(!memberService.isKakaoMember(kakaoUserkey)) throw new NullPointerException("회원이아닙니다.");
            if(memberService.isWithdrawalKakaoMember(kakaoUserkey))  throw new NullPointerException("이미 탈퇴 회원입니다.");
            memberService.Withdrawal(kakaoUserkey);
            msg="회원탈퇴성공";
            result= URLEncoder.encode(msg,"UTF-8");
            redirectURL.put("redirectURL",kakaoChannelUrl+"/"+result);
            return redirectURL;
        }catch (Exception e){
            e.printStackTrace();
            msg = e.getMessage();
            result= URLEncoder.encode(msg,"UTF-8");
            redirectURL.put("redirectURL",kakaoChannelUrl+"/"+result);
            return redirectURL;
        }
    }

    @GetMapping(value = "kakaopay-ready")
    public String kakaoPayReady(@RequestParam Long itemcode,@RequestParam String kakaouserkey, @RequestParam Long optionId, @RequestParam int totalPrice, @RequestParam int quantity) throws Exception{
        log.info("itemcode={},kakaouserkey={}",itemcode,kakaouserkey);
        String redirectUrl;
        String result;
        try {
            redirectUrl = openApiService.kakaoPayReady(itemcode,optionId,totalPrice,quantity, kakaouserkey);
        }catch (Exception e){
            e.printStackTrace();
            result= URLEncoder.encode("결제실패","UTF-8");
            redirectUrl=kakaoChannelUrl+"/"+result;
            return "redirect:"+redirectUrl;
        }
        return "redirect:"+redirectUrl;
    }

    @GetMapping(value = "/{orderId}/kakaopay-approve")
    public String kakaoPayApprove (@PathVariable Long orderId, String pg_token) throws Exception{
        String result;
        try {
            log.info("pg_token={} orderId={}",pg_token,orderId);
            openApiService.kakaoPayApprove(pg_token,orderId);
        }catch (Exception e){
            e.printStackTrace();
            result= URLEncoder.encode("결제실패","UTF-8");
            return "redirect:"+kakaoChannelUrl+"/"+result;
        }
        result= URLEncoder.encode("결제완료","UTF-8");
        return "redirect:"+kakaoChannelUrl+"/"+result;
    }

    @GetMapping(value = "/{orderId}/kakaopay-cancel")
    public String kakaoPayCancel (@PathVariable Long orderId) throws Exception {
        String result;
        try {
            openApiService.kakaoPayCancel(orderId);
        }catch (Exception e){
            e.printStackTrace();
            result= URLEncoder.encode("주문취소실패","UTF-8");
            return "redirect:"+kakaoChannelUrl+"/"+result;
        }
        result= URLEncoder.encode("주문취소완료","UTF-8");
        return "redirect:"+kakaoChannelUrl+"/"+result;
    }

    @ResponseBody
    @PostMapping(value = "location-agree" , headers = {"Accept=application/json; UTF-8"})
    public HashMap<String, String> locationAgree(@RequestBody MemberLocationDto memberLocationDto){
        HashMap<String,String> redirectURL = new HashMap<>();
        try {
            MemberDto memberDto = MemberDto.builder()
                    .kakaoUserkey(memberLocationDto.getKakaoUserkey())
                    .build();
            memberService.join(memberDto); //<==회원 체크 및 회원 가입

            KakaoMemberLocation kakaoMemberLocation = memberLocationDto.toEntity();
            memberService.saveLocation(kakaoMemberLocation); //<== 위치정보 저장 및 업데이트
            redirectURL.put("redirectURL",kakaoChannelUrl+"/위치정보 동의 완료");
            return redirectURL;
        }catch (Exception e){
            e.printStackTrace();
            redirectURL.put("redirectURL",kakaoChannelUrl+"/위치정보 동의 실패");
            return redirectURL;
        }
    }
    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }
}
