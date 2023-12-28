package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.MemberService;

@Controller
@RequestMapping(value = "admin/member")
public class MemberController {

    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse join(@RequestBody Member member){
        String name = member.getName();
        String userKey = member.getUserKey();
        String phoneNumber = member.getPhoneNumber();
        return memberService.join(userKey, name, phoneNumber);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public HttpResponse getMembers(){
        return memberService.getMembers();
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.GET)
    public HttpResponse getMember(@PathVariable String memberUserKey){
        return memberService.getMember(memberUserKey);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse updateMember(@PathVariable String memberUserKey, @RequestBody Member member){
        return memberService.updateMember(memberUserKey,member);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public HttpResponse deleteMember(@PathVariable String memberUserKey){
        return memberService.deleteMember(memberUserKey);
    }
}
