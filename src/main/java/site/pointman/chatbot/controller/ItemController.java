package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ItemService;

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
        log.info("request::={}",reqDto.getUserKey());
        try {

            JSONObject itemName = itemService.createItemName(reqDto);
            return itemName;
        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return authService.createFailMessage("서비스오류가 발생하였습니다.");
        }

    }

    @ResponseBody
    @RequestMapping(value = "add/itemImage", method = RequestMethod.POST, headers = {"Accept=application/json; UTF-8"})
    public JSONObject itemImageAdd(@RequestBody RequestDto reqDto) throws Exception {
        log.info("request::={}",reqDto.getImage());

        return null;
    }
}
