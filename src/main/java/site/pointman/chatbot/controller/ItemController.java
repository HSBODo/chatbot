package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ItemService;
import site.pointman.chatbot.vo.ContextsVo;
import site.pointman.chatbot.vo.ValidateApiResponse;
import site.pointman.chatbot.vo.ValidationApiStatusType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/kakaoChannel/item/*")
public class ItemController {
    AuthService authService;
    ItemService itemService;

    public ItemController(AuthService authService, ItemService itemService) {
        this.authService = authService;
        this.itemService = itemService;
    }

    @ResponseBody
    @RequestMapping(value = "add/authChk", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject memberAuthChk(@RequestBody RequestDto reqDto) throws Exception {
        log.info("request::={}",reqDto.getUserKey());
        try {
            boolean authMember = authService.isAuthMember(reqDto.getUserKey());

            if(!authMember){
                JSONObject authForm = authService.createAuthForm(reqDto);
                return authForm;
            }

            JSONObject notice = itemService.createNotice(reqDto);

            return notice;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }

    }

    @ResponseBody
    @RequestMapping(value = "add/name", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject itemNameAdd(@RequestBody RequestDto reqDto) throws Exception {
        log.info("controller={}","itemNameAdd");
        log.info("utterance={}",reqDto.getUtterance());
        log.info("userKey={}",reqDto.getUserKey());


//        ServletInputStream inputStream = req.getInputStream();
//        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//        log.info("messageBody={}", messageBody);




        try {

            JSONObject itemName = itemService.createItemName(reqDto);
            return itemName;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }

    }

    @ResponseBody
    @RequestMapping(value = "validate/name", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public String itemNameValidate(@RequestBody RequestDto reqDto) throws Exception {
        log.info("uttr={}",reqDto.getUtterance());
        String value = reqDto.getUtterance();
        Map<String,String> data = new HashMap<>();
        data.put("","");
        log.info("value={}",value);
        //ContextsVo contexts = reqDto.getContexts();
        //log.info("contexts={}",contexts.getName());

        ValidateApiResponse response = new ValidateApiResponse(ValidationApiStatusType.SUCCESS,value,data);
        return response.createJsonResponse();
    }

    @ResponseBody
    @RequestMapping(value = "add/itemImage", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject itemImageAdd(@RequestBody RequestDto reqDto) throws Exception {
        log.info("request::={}",reqDto.getImage());

        return null;
    }
}
