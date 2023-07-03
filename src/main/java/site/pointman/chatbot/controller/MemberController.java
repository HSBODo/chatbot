package site.pointman.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.BarcodeDto;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.OpenApiService;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/kakaoChannel/member/*")
public class MemberController {

    private AuthService authService;
    private OpenApiService openApiService;

    public MemberController(AuthService authService, OpenApiService openApiService) {
        this.authService = authService;
        this.openApiService = openApiService;
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

        log.info("step={}",req.getStep());
        log.info("timezone={}",req.getTimezone());
        log.info("lang={}",req.getLang());
        log.info("userKey={}",req.getUserKey());


        return null;

    }

    @ResponseBody
    @RequestMapping(value = "profile" , headers = {"Accept=application/json; UTF-8"})
    public JSONObject profile(@RequestBody RequestDto req) throws Exception {


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(req.getProfile(), Map.class);

        String otp = map.get("otp");
        String restApiKey = "8a2a17e6da8534c21ea0af2072545aeb";

        MemberDto memberProfile = openApiService.getMemberProfile(otp, restApiKey);

        return authService.createProfileSuccessMessage("프로필 플러그인 정보\n이름: "+memberProfile.getName()+"\n전화번호: "+memberProfile.getPhone());

    }
}
