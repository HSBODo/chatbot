package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.member.Member;
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
    public Object join(@RequestBody Member member){
        String name = member.getName();
        String userKey = member.getUserKey();
        String phoneNumber = member.getPhoneNumber();
        return memberService.join(name,userKey,phoneNumber,false);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.GET)
    public Object getMember(@PathVariable String memberUserKey){
        return memberService.getCustomerProfile(memberUserKey,false);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.PATCH)
    public Object updateMemberPhoneNumber(@PathVariable String memberUserKey, @RequestBody String phoneNumber){
        return memberService.updateCustomerPhoneNumber(memberUserKey,phoneNumber,false);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public Object deleteMember(@PathVariable String memberUserKey){
        return memberService.withdrawalCustomer(memberUserKey,false);
    }

}
