package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.MemberService;

@Controller
@RequestMapping(value = "admin/member", consumes = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Response join(@RequestBody Member member){
        String name = member.getName();
        String userKey = member.getUserKey();
        String phoneNumber = member.getPhoneNumber();
        return memberService.join(name,userKey,phoneNumber,false);
    }
}
