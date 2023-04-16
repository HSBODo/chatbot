package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/admin-page/*")
public class AdminWebController {
    @GetMapping("/login")
    public String login(){
        log.info("login");
        return "admin/login";
    }

    @GetMapping("/join")
    public String join(){
        log.info("join");
        return "admin/join";
    }

}
