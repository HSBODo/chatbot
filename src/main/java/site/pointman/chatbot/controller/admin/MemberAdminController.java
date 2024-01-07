package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.member.dto.MemberJoinDto;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.globalservice.ValidationService;

import java.nio.charset.Charset;

@Slf4j
@Controller
@RequestMapping(value = "admin/member")
public class MemberAdminController {

    MemberService memberService;
    ValidationService validationService;


    public MemberAdminController(MemberService memberService, ValidationService validationService) {
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

        memberService.join(userKey, name, phoneNumber);

        return new ResponseEntity<>(new Response(ResultCode.OK,"성공적으로 회원가입을 완료하였습니다."),headers ,HttpStatus.OK);
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
        try {
            MemberProfileDto memberProfileDtoByName = memberService.getMemberProfileDtoByName(memberName);

            return new ResponseEntity<>(new Response<>(ResultCode.OK,"성공적으로 회원을 조회하였습니다.",memberProfileDtoByName),headers ,HttpStatus.OK);
        }catch (NotFoundMember e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"회원 조회를 실패하였습니다",e.getMessage()),headers ,HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "{memberName}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMember(@PathVariable String memberName, @RequestBody MemberProfileDto memberProfileDto){
        HttpHeaders headers = getHeaders();
        try {
            memberService.updateMemberProfile(memberName, memberProfileDto);

            return new ResponseEntity<>("성공적으로 회원정보를 수정하였습니다.",headers ,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"회원정보 수정을 실패하였습니다.",e.getMessage()),headers ,HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "{memberUserKey}",method = RequestMethod.DELETE)
    public ResponseEntity deleteMember(@PathVariable String memberUserKey){
        HttpHeaders headers = getHeaders();
        try {
            memberService.deleteMember(memberUserKey);

            return new ResponseEntity<>("성공적으로 회원탈퇴를 완료하였습니다.",headers ,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"회원탈퇴를 실패하였습니다",e.getMessage()),headers ,HttpStatus.OK);
        }

    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
