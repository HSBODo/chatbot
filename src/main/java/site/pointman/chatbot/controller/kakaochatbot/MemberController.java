package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.handler.annotation.ValidateMember;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.view.kakaochatobotview.MemberChatBotView;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class MemberController {

    MemberService memberService;
    MemberChatBotView memberChatBotView;

    ChatBotExceptionResponse chatBotExceptionResponse;

    public MemberController(MemberService memberService, MemberChatBotView memberChatBotView) {
        this.memberService = memberService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
        this.memberChatBotView = memberChatBotView;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/join" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse join(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String name = chatBotRequest.getCustomerName();
        String phoneNumber = chatBotRequest.getCustomerPhone();

        if (memberService.isCustomer(userKey)) return chatBotExceptionResponse.createException("이미 존재하는 회원입니다.");

        return memberChatBotView.joinMemberResultPage(userKey, name, phoneNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "PATCH/profileImage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProfileImage(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String customerProfileImage = chatBotRequest.getCustomerProfileImages().get(0);

        return memberChatBotView.updateMemberProfileImageResultPage(userKey,customerProfileImage);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/myPage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyPage(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return memberChatBotView.myPage();
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return memberChatBotView.myProfilePage(userKey);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/salesCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getSalesHistory(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return memberChatBotView.mySalesCategoryListPage();
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "PATCH/phoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String updatePhoneNumber = chatBotRequest.getCustomerPhone();

        return memberChatBotView.updateMemberPhoneNumberResultPage(userKey, updatePhoneNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse withdrawalCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return memberChatBotView.withdrawalMemberResultPage(userKey);
    }
}
