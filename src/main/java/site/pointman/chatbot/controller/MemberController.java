package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/kakaoChannel/member/*")
public class MemberController {

    private AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseBody
    @RequestMapping(value = "auth", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberAuth(@RequestBody RequestDto reqDto) throws Exception {
        log.info("request::={}",reqDto.getUserKey());
        try {
            JSONObject authForm = authService.createAuthForm(reqDto);
            return authForm;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }

    }
    @ResponseBody
    @RequestMapping(value = "authCancel", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberAuthCancel(@RequestBody RequestDto reqDto) throws Exception {

        log.info("request::={}",reqDto.getUserKey());
        try {
            JSONObject authCancel = authService.createAuthCancel(reqDto);
            return authCancel;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }
    }
    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberInfo(@RequestBody RequestDto reqDto) throws Exception {

        log.info("request::={}",reqDto.getUserKey());
        try {
            boolean isAuthMember = authService.isAuthMember(reqDto.getUserKey());
            if(!isAuthMember){
                JSONObject failMessage = authService.createFailMessage("인증 고객이 아닙니다.");
                return failMessage;
            }
            JSONObject authInfoForm = authService.createAuthInfo(reqDto);
            return authInfoForm;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }
    }

    @ResponseBody
    @RequestMapping(value = "barCode" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject barCode(@RequestBody BarcodeDto req) throws Exception {

        log.info("request::={}",req.getOrigin());

        return null;

    }

    @ResponseBody
    @RequestMapping(value = "image" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject image(@RequestBody String req) throws Exception {

        log.info("request::={}",req);


        return null;

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


        return null;

    }
}
