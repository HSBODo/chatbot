package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.annotation.SkipAop;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.login.Login;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.MemberService;

@Controller
@RequestMapping(value = "login")
public class LoginController {

    MemberService memberService;
    AuthService authService;

    public LoginController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @ResponseBody
    @SkipAop
    @PostMapping(value = "")
    public HttpResponse login(@RequestBody Login login){
        boolean isAdmin = memberService.isAdmin(login.getName(), login.getUserKey());
        if (!isAdmin) return new HttpResponse(ApiResultCode.FAIL,"이름과 유저키가 옳바르지 않습니다.");

        String jwtToken = authService.createJwtToken(login.getName(), login.getUserKey());

        return new HttpResponse(ApiResultCode.OK,"로그인 성공 token=["+jwtToken+"]");
    }
}
