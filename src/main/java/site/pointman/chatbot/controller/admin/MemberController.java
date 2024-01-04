package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.member.dto.MemberJoinDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.service.ValidationService;

import java.nio.charset.Charset;
import java.util.Optional;

@Slf4j
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
    public ResponseEntity join(@RequestBody MemberJoinDto memberJoinDTO){
        HttpHeaders headers = getHeaders();

        String name = memberJoinDTO.getName();
        String userKey = memberJoinDTO.getUserKey();
        String phoneNumber = memberJoinDTO.getPhoneNumber();

        if (!validationService.isValidMemberName(name)) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"중복되는 이름입니다."),headers, HttpStatus.OK);
        if (!validationService.isValidMemberPhoneNumber(phoneNumber)) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"연락처 형식이 옳바르지 않습니다."),headers, HttpStatus.OK);
        if (memberService.isCustomer(userKey)) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"이미 회원가입된 유저입니다."),headers, HttpStatus.OK);

        Response response = memberService.join(userKey, name, phoneNumber);
        return new ResponseEntity<>(response,headers ,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity getMembers(@RequestParam("page") int page) {
        HttpHeaders headers = getHeaders();

        Page<MemberProfileDto> members = memberService.getMemberProfiles(page);

        if (members.getContent().isEmpty()) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"회원이 존재하지 않습니다"),headers, HttpStatus.OK);

        return new ResponseEntity<>(members.getContent(),headers ,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{memberName}",method = RequestMethod.GET)
    public ResponseEntity getMember(@PathVariable String memberName){
        HttpHeaders headers = getHeaders();

        Optional<MemberProfileDto> mayBeMember = memberService.getMemberProfileDtoByName(memberName);
        if (mayBeMember.isEmpty()) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"회원이 존재하지 않습니다."),headers, HttpStatus.OK);
        MemberProfileDto memberProfileDto = mayBeMember.get();


        return new ResponseEntity<>(memberProfileDto,headers ,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{memberName}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMember(@PathVariable String memberName, @RequestBody MemberProfileDto memberProfileDto){
        HttpHeaders headers = getHeaders();

        Response response = memberService.updateMember(memberName, memberProfileDto);

        return new ResponseEntity<>(response,headers ,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public ResponseEntity deleteMember(@PathVariable String memberUserKey){
        HttpHeaders headers = getHeaders();

        Response response = memberService.deleteMember(memberUserKey);

        return new ResponseEntity<>(response,headers ,HttpStatus.OK);
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
