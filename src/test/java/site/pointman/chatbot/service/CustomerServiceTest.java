package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    MemberService memberService;

    private ObjectMapper mapper = new ObjectMapper();
    private ChatBotRequest chatBotRequest;
    private String userKey;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        userKey = "QFJSyeIZbO77";
    }

    @Test
    @Transactional
    void join() throws JsonProcessingException {
        //give
        String userKey = "유저키";
        String name = "이름";
        String phoneNumber = "01000001111";

        //when
        Object response = memberService.join(userKey,name,phoneNumber,true);
        ChatBotResponse chatBotResponse = (ChatBotResponse) response;
        String responseMessage = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();
        //then
        Assertions.assertThat(responseMessage).isEqualTo("회원가입이 완료 되었습니다.");
    }

    @Test
    @Transactional
    void getCustomerInfo() {
        //give

        //when
        Object response = memberService.getCustomerProfile(userKey, true);
        ChatBotResponse chatBotResponse = (ChatBotResponse) response;
        String responseTitle = chatBotResponse.getTemplate().getOutputs().get(0).getTextCard().getTitle();

        //then
        Assertions.assertThat(responseTitle).isEqualTo("회원정보");
    }

    @Test
    @Transactional
    void updateCustomerPhoneNumber() {
        //give
        String updatePhoneNumber ="01011112222";

        //when
        Object response = memberService.updateCustomerPhoneNumber(userKey,updatePhoneNumber,true);
        ChatBotResponse chatBotResponse = (ChatBotResponse) response;
        String responseMessage = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(responseMessage).isEqualTo("연락처 변경이 완료 되었습니다.");
    }

    @Test
    @Transactional
    void deleteCustomer() {
        //give

        //when
        Object response = memberService.withdrawalCustomer(userKey, true);
        ChatBotResponse chatBotResponse = (ChatBotResponse) response;
        String responseMessage = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(responseMessage).isEqualTo("회원탈퇴가 완료 되었습니다.");
    }

    @Test
    @Transactional
    void isCustomer() {
        //give

        //when
        boolean isCustomer = memberService.isCustomer(userKey);
        //then
        Assertions.assertThat(isCustomer).isTrue();
    }
}