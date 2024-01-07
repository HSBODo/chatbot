package site.pointman.chatbot.controller.kakaochatbot;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.member.dto.MemberJoinDto;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.handler.annotation.ValidateMember;
import site.pointman.chatbot.view.kakaochatobotview.MemberChatBotView;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kakaochatbot/customer")
public class MemberController {

    private final MemberService memberService;
    private final MemberChatBotView memberChatBotView;

    private final ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @PostMapping(value = "POST/join" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse join(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            MemberJoinDto memberJoinDto = chatBotRequest.getMemberJoinDto();

            if (memberService.isCustomer(memberJoinDto.getUserKey())) return chatBotExceptionResponse.createException("이미 존재하는 회원입니다.");

            memberService.join(memberJoinDto.getUserKey(), memberJoinDto.getName(), memberJoinDto.getPhoneNumber());

            return memberChatBotView.joinMemberResultPage();
        }catch (Exception e) {
            return chatBotExceptionResponse.createException("회원가입에 실패하였습니다.");
        }
    }

    @ValidateMember
    @PostMapping(value = "PATCH/profileImage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProfileImage(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();
            String customerProfileImage = chatBotRequest.getCustomerProfileImage();

            memberService.updateMemberProfileImage(userKey,customerProfileImage);

            return memberChatBotView.updateMemberProfileImageResultPage();
        }catch (Exception e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }
    }

    @ValidateMember
    @PostMapping(value = "GET/myPage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyPage(@RequestBody ChatBotRequest chatBotRequest) {

        return memberChatBotView.myPage();
    }

    @ValidateMember
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProfile(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();

            MemberProfileDto memberProfileDto = memberService.getMemberProfileDto(userKey);

            return memberChatBotView.myProfilePage(memberProfileDto);
        }catch (NotFoundMember e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }
    }

    @ValidateMember
    @PostMapping(value = "GET/salesCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getSalesHistory(@RequestBody ChatBotRequest chatBotRequest) {

        return memberChatBotView.mySalesCategoryListPage();
    }

    @ValidateMember
    @PostMapping(value = "PATCH/phoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();
            String updatePhoneNumber = chatBotRequest.getCustomerPhone();

            memberService.updateMemberPhoneNumber(userKey,updatePhoneNumber);

            return memberChatBotView.updateMemberPhoneNumberResultPage();
        }catch (NotFoundMember e) {
           return chatBotExceptionResponse.createException(e.getMessage());
        }
    }

    @ValidateMember
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse withdrawalCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();

            memberService.deleteMember(userKey);

            return memberChatBotView.withdrawalMemberResultPage();
        }catch (Exception e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }
    }
}
