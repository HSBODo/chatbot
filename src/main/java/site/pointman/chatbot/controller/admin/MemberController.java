package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.member.Member;
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
    public Object join(@RequestBody Member member){
        String name = member.getName();
        String userKey = member.getUserKey();
        String phoneNumber = member.getPhoneNumber();
        return memberService.join(name,userKey,phoneNumber);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Object getMembers(){
        return memberService.getMembers();
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.GET)
    public Object getMember(@PathVariable String memberUserKey){
        return memberService.getMember(memberUserKey);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object updateMember(@PathVariable String memberUserKey, @RequestBody Member member){
        return memberService.updateMember(memberUserKey,member);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public Object deleteMember(@PathVariable String memberUserKey){
        return memberService.deleteMember(memberUserKey);
    }
}
