package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/*")
public class KakaoRestAPI {

    @Value("${kakao.channel.url}")
    private String kakaoChannelUrl;






    @ResponseBody
    @RequestMapping(value = "login" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto login(@RequestBody RequestDto request) throws Exception {
        ResponseDto response = new ResponseDto();
        try {
            return response;
        }catch (Exception e){
            return response;
        }
    }


    @GetMapping(value = "kakaoMemeberDeleteForm")
    public String kakaoMemberDeleteForm(){
        return "member/kakaoDeleteForm";
    }





    @GetMapping(value = "location-notice")
    public String locationNotice(){
        log.info("-----------위치정보 동의---------");
        return "location-notice";
    }
}
