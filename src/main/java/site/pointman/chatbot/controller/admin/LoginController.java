package site.pointman.chatbot.controller.admin;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.member.dto.LoginDto;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.globalservice.AuthService;
import site.pointman.chatbot.domain.member.service.MemberService;

import java.nio.charset.Charset;

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
    @PostMapping(value = "")
    public ResponseEntity login(@RequestBody LoginDto login){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        boolean isAdmin = memberService.isAdmin(login.getName(), login.getUserKey());
        if (!isAdmin) return  new ResponseEntity<>("옳바르지 않은 정보입니다.",headers, HttpStatus.OK);


        String jwtToken = authService.createJwtToken(login.getName(), login.getUserKey());
        Response response = new Response(ResultCode.OK,"로그인에 성공하였습니다",jwtToken);

        return new ResponseEntity<>(response,headers ,HttpStatus.OK);
    }
}
