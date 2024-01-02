package site.pointman.chatbot.controller.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.dto.member.MemberJoinDTO;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.ValidationService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/member")
public class MemberController {

    MemberService memberService;
    ValidationService validationService;


    public MemberController(MemberService memberService, ValidationService validationService) {
        this.memberService = memberService;
        this.validationService = validationService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response join(@RequestBody MemberJoinDTO memberJoinDTO){
        String name = memberJoinDTO.getName();
        String userKey = memberJoinDTO.getUserKey();
        String phoneNumber = memberJoinDTO.getPhoneNumber();

        if (!validationService.isValidMemberName(name)) return new Response(ResultCode.EXCEPTION,"중복되는 이름입니다.");
        if (!validationService.isValidMemberPhoneNumber(phoneNumber)) return new Response(ResultCode.EXCEPTION,"연락처 형식이 옳바르지 않습니다.");
        if (memberService.isCustomer(userKey)) return new Response(ResultCode.EXCEPTION,"이미 회원가입된 유저입니다.");

        return memberService.join(userKey, name, phoneNumber);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Object getMembers() {
        return memberService.getMembers();
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.GET)
    public Object getMember(@PathVariable String memberUserKey){
        return memberService.getMember(memberUserKey);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateMember(@PathVariable String memberUserKey, @RequestBody Member member){
        return memberService.updateMember(memberUserKey,member);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public Response deleteMember(@PathVariable String memberUserKey){
        return memberService.deleteMember(memberUserKey);
    }
}
