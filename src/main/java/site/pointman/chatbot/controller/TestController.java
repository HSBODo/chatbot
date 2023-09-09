package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.dto.KakaoRequestDto;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.request.RequestDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/test/*")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "req" , headers = {"Accept=application/json; UTF-8"})
    public RequestDto callAPI(@RequestBody RequestDto request) throws Exception {

        return request;
    }

    @ResponseBody
    @RequestMapping(value = "str" , headers = {"Accept=application/json; UTF-8"})
    public String str(@RequestBody String request) throws Exception {
        log.info("request::={}",request);
        return request;
    }

}
