package site.pointman.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.MemberRepository;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "/admin-page/*")
public class AdminWebController {
    MemberRepository memberRepository;

    public AdminWebController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/")
    public String home(){
        log.info("login");
        return "admin/index";
    }
    @GetMapping("/login")
    public String loginForm(){
        log.info("login");
        return "admin/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password){
        log.info("id={}, password={}",id,password);
        try {
            Member member = memberRepository.findByMember(id).get();
            if(!member.getPassword().equals(password)) throw new IllegalStateException("비밀번호가 틀립니다.");

        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/admin-page/login";
        }

        return "redirect:/admin-page/";
    }
    @GetMapping("/join")
    public String joinForm(@ModelAttribute Member member){
        log.info("join");
        return "admin/joinForm";
    }
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute Member member, BindingResult bindingResult){
        log.info("member={}",member);
        if(bindingResult.hasErrors()){
            return "admin/joinForm";
        }

        memberRepository.save(member);
        return "redirect:/admin-page/";
    }
}
