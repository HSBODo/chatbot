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
import site.pointman.chatbot.dto.*;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.AddressRepository;
import site.pointman.chatbot.repository.BlockRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.*;

import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/kakaochat/v1/*")
public class KakaoRestAPI {
    @Value("${kakao.channel.url}")
    private String kakaoChannelUrl;
    private KakaoApiService kakaoApiService;
    private MemberService memberService;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private BlockService blockService;
    private BlockRepository blockRepository;
    private KakaoMemberRepository kakaoMemberRepository;
    private AddressRepository addressRepository;
    private JSONParser jsonParser = new JSONParser();

    public KakaoRestAPI(KakaoApiService kakaoApiService, MemberService memberService, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService, BlockService blockService, BlockRepository blockRepository, KakaoMemberRepository kakaoMemberRepository, AddressRepository addressRepository) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.blockService = blockService;
        this.blockRepository = blockRepository;
        this.kakaoMemberRepository = kakaoMemberRepository;
        this.addressRepository = addressRepository;
    }

    @ResponseBody
    @RequestMapping(value = "chat" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject callAPI(@RequestBody KakaoRequestDto request) throws Exception {
        JSONObject response = new JSONObject();
        try {
            String uttr = request.getUttr(); //사용자 발화
            String kakaoUserkey = request.getKakaoUserkey(); //사용자 유저키
            JSONObject buttonParams = request.getButtonParams(); //버튼 파라미터
            Long blockId = request.getBlockId(); //블럭Id
            BlockServiceType service = request.getBlockService(); //블럭 서비스
            log.info("Request:: uttr ={}, userkey = {} buttonParams={}",uttr,kakaoUserkey,buttonParams);

            BlockDto blockDto = BlockDto.builder()
                    .id(blockId)
                    .service(service)
                    .build();
            Optional<KakaoMember> maybeMember = kakaoMemberRepository.findByMember(kakaoUserkey);
            if(maybeMember.isEmpty()) blockDto.setService(BlockServiceType.회원가입);

            if(request.getBlockService() != null) memberService.saveAttribute(buttonParams,kakaoUserkey); //<==선택한 버튼 파라미터 저장

            if(blockDto.getService()==null){ //<== 사용자 발화로 서비스 조회
                Map<String,BlockServiceType> blockServiceMapping = new HashMap<>();
                blockServiceMapping.put("판매상품",BlockServiceType.상품조회);
                blockServiceMapping.put("주문조회",BlockServiceType.주문조회);
                blockServiceMapping.put("배송지등록완료",BlockServiceType.주문서);
                BlockServiceType findBlockServiceType = blockServiceMapping.get(uttr);
                blockDto.setService(findBlockServiceType);
            }
            if(blockDto.getService()==null) return null; //<== 버튼 파라미터 서비스가 존재하지 않고 발화 서비스가 존재하지않으면 응답 없음

             response = blockService.findByService(kakaoUserkey, blockDto, buttonParams);

        }catch (Exception e){
            e.printStackTrace();
            KakaoResponseDto kakaoResponse = new KakaoResponseDto();
            kakaoResponse.addContent(kakaoJsonUiService.createSimpleText(e.getMessage()));
            return kakaoResponse.createKakaoResponse();
        }
        log.info("kakaoResponse = {}", response);
        return response;
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
            redirectURL.put("redirectURL",kakaoChannelUrl+"/위치정보 동의 완료");
        }catch (Exception e){
            e.printStackTrace();
            redirectURL.put("redirectURL",kakaoChannelUrl+"/위치정보 동의 실패");
        }
        return redirectURL;
    }
    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }
}
