package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.dto.BarcodeDto;
import site.pointman.chatbot.dto.KakaoRequestDto;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.ButtonType;
import site.pointman.chatbot.dto.kakaoui.DisplayType;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/kakaoChannel/member/*")
public class MemberController {

    private KakaoJsonUiService kakaoJsonUiService;

    public MemberController(KakaoJsonUiService kakaoJsonUiService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @ResponseBody
    @RequestMapping(value = "auth" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberAuth(@RequestBody String req) throws Exception {

        log.info("request::={}",req);


        KakaoResponseDto response = new KakaoResponseDto();
        List<ButtonDto> buttons = new ArrayList<>();
        ButtonDto selectAddressButton = new ButtonDto(ButtonType.webLink,"인증하기","https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg");
        buttons.add(selectAddressButton);
        response.addContent(kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "인증하기",
                "인증이 필요합니다.",
                "https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg",
                buttons));
        ButtonDto quickButton = new ButtonDto();
        Map<String,String> params = new HashMap<>();
        params.put("opt1","1");
        params.put("opt2","2");

        response.addQuickButton(quickButton.createButtonBlock("인증완료","649911032e776341af591d70",params));
        return response.createKakaoResponse();

    }
    @ResponseBody
    @RequestMapping(value = "authCancel" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberAuthCancel(@RequestBody String req) throws Exception {

        log.info("request::={}",req);


        KakaoResponseDto response = new KakaoResponseDto();
        response.addContent(kakaoJsonUiService.createSimpleText(
              "인증철회 완료"));
        ButtonDto quickButton = new ButtonDto(ButtonType.block,"처음으로","");
        response.addQuickButton(quickButton);
        return response.createKakaoResponse();

    }
    @ResponseBody
    @RequestMapping(value = "info" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberInfo(@RequestBody String req) throws Exception {
        KakaoResponseDto response = new KakaoResponseDto();
        log.info("request::={}",req);



        List<ButtonDto> buttons = new ArrayList<>();
        ButtonDto button = new ButtonDto();
        ButtonDto buttonBlock = button.createButtonBlock("인증철회","64991a325a9dc36fa608b667");
        buttons.add(buttonBlock);
        MemberDto memberDto = MemberDto.builder()
                .name("홍길동")
                .phone("01000000000")
                .build();

        response.addContent(kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "인증정보",
                "이름: "+memberDto.getName()+"\n"+
                        "연락처: "+memberDto.getPhone()+"\n",
                "https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg",
                buttons));
        ButtonDto quickButton = new ButtonDto();
        ButtonDto quickButton2 = new ButtonDto();
        Map<String,String>params = new HashMap<>();
        params.put("test","123123");
        response.addQuickButton(quickButton.createButtonBlock("처음으로","64993967368ce63259b3faca"));
        response.addQuickButton(quickButton2.createButtonBlock("인증정보","649911032e776341af591d70",params));
        return response.createKakaoResponse();

    }

    @ResponseBody
    @RequestMapping(value = "barCode" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject barCode(@RequestBody BarcodeDto req) throws Exception {

        log.info("request::={}",req.getOrigin());
        String barcode= req.getOrigin();
        barcode= barcode.replaceAll("\"","");
        KakaoResponseDto response = new KakaoResponseDto();
        response.addContent(kakaoJsonUiService.createSimpleText(
                ""+barcode+""));
        ButtonDto quickButton = new ButtonDto(ButtonType.block,"처음으로","");
        response.addQuickButton(quickButton);
        return response.createKakaoResponse();

    }

    @ResponseBody
    @RequestMapping(value = "image" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject image(@RequestBody String req) throws Exception {

        log.info("request::={}",req);

        KakaoResponseDto response = new KakaoResponseDto();
        response.addContent(kakaoJsonUiService.createSimpleText(
                "이미지 등록완료"));
        ButtonDto quickButton = new ButtonDto(ButtonType.block,"처음으로","");
        response.addQuickButton(quickButton);
        return response.createKakaoResponse();

    }

    @ResponseBody
    @RequestMapping(value = "mapping" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject mapping(@RequestBody RequestDto req) throws Exception {

        log.info("blockId={}",req.getBlockId());
        log.info("blockName={}",req.getBlockName());
        log.info("botId={}",req.getBotId());
        log.info("botName={}",req.getBotName());
        log.info("skillId={}",req.getSkillId());
        log.info("skillName={}",req.getSkillName());
        log.info("param={}",req.getParam());
        log.info("step={}",req.getStep());
        log.info("timezone={}",req.getTimezone());
        log.info("lang={}",req.getLang());
        log.info("userKey={}",req.getUserKey());

        KakaoResponseDto response = new KakaoResponseDto();
        response.addContent(kakaoJsonUiService.createSimpleText(
                "이미지 등록완료"));
        ButtonDto quickButton = new ButtonDto(ButtonType.block,"처음으로","");
        response.addQuickButton(quickButton);
        return response.createKakaoResponse();

    }
}
