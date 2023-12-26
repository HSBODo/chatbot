package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.chatbot.CustomerChatBotResponseService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class CustomerController {

    MemberService memberService;
    CustomerChatBotResponseService customerChatBotResponseService;

    ChatBotExceptionResponse chatBotExceptionResponse;

    public CustomerController(MemberService memberService, CustomerChatBotResponseService customerChatBotResponseService) {
        this.memberService = memberService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
        this.customerChatBotResponseService = customerChatBotResponseService;
    }

    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ResponseBody
    @PostMapping(value = "POST/join" , headers = {"Accept=application/json; UTF-8"})
    public Object join(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String name = chatBotRequest.getCustomerName();
        String phoneNumber = chatBotRequest.getCustomerPhone();

        if (memberService.isCustomer(userKey)) return chatBotExceptionResponse.createException("이미 존재하는 회원입니다.");

        return customerChatBotResponseService.joinChatBotResponse(userKey, name, phoneNumber);
    }

    @ResponseBody
    @PostMapping(value = "PATCH/profileImage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProfileImage(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String customerProfileImage = chatBotRequest.getCustomerProfileImages().get(0);


        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.updateCustomerProfileImageChatBotResponse(userKey,customerProfileImage);
    }

    @ResponseBody
    @PostMapping(value = "GET/myPage" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyPage(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.getMyPageSuccessChatBotResponse();
    }

    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public Object getProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.getCustomerProfileChatBotResponse(userKey);
    }

    @ResponseBody
    @PostMapping(value = "GET/salesCategory" , headers = {"Accept=application/json; UTF-8"})
    public Object getSalesHistory(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.getSalesCategorySuccessChatBotResponse();
    }

    @ResponseBody
    @PostMapping(value = "PATCH/phoneNumber" , headers = {"Accept=application/json; UTF-8"})
    public Object updatePhoneNumber(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String updatePhoneNumber = chatBotRequest.getCustomerPhone();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.updateCustomerPhoneNumberBotResponse(userKey, updatePhoneNumber);
    }

    @ResponseBody
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public Object withdrawalCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();

        return customerChatBotResponseService.withdrawalCustomerChatBotResponse(userKey);
    }
}
