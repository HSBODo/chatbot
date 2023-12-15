package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    CustomerService customerService;

    private ObjectMapper mapper = new ObjectMapper();
    private ChatBotRequest chatBotRequest;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        String userKey = "QFJSyeIZbO77";
        String customerPhone = "01000001111";
        String body="{\"intent\":{\"id\":\"6576dfef287a164bd6cd8f62\",\"name\":\"회원 연락처 변경\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"6576dfef287a164bd6cd8f62\",\"name\":\"회원 연락처 변경\"},\"utterance\":\"연락처변경\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\""+userKey+"\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"회원_연락처_변경\",\"clientExtra\":{},\"params\":{\"customerPhone\":\""+customerPhone+"\"},\"id\":\"6576ec1ba7e99f5af9c44319\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, ChatBotRequest.class);

    }

    @Test
    @Transactional
    void join() throws JsonProcessingException {
        //give
        String userKey = "유저키";
        String customerPhone = "01000001111";
        String body="{\"intent\":{\"id\":\"6576dfef287a164bd6cd8f62\",\"name\":\"회원 연락처 변경\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"6576dfef287a164bd6cd8f62\",\"name\":\"회원 연락처 변경\"},\"utterance\":\"연락처변경\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\""+userKey+"\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"회원_연락처_변경\",\"clientExtra\":{},\"params\":{\"customerPhone\":\""+customerPhone+"\"},\"id\":\"6576ec1ba7e99f5af9c44319\",\"detailParams\":{}},\"contexts\":[]}";
        ChatBotRequest chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, ChatBotRequest.class);

        //when
        ChatBotResponse response = customerService.join(chatBotRequest);
        String responseMessage = response.getTemplate().getOutputs().get(0).getSimpleText().getText();
        //then
        Assertions.assertThat(responseMessage).isEqualTo("회원가입이 완료 되었습니다.");
    }

    @Test
    @Transactional
    void getCustomerInfo() {
        //give

        //when
        ChatBotResponse response = customerService.getCustomerInfo(chatBotRequest);
        String responseTitle = response.getTemplate().getOutputs().get(0).getTextCard().getTitle();

        //then
        Assertions.assertThat(responseTitle).isEqualTo("회원정보");
    }

    @Test
    @Transactional
    void updateCustomerPhoneNumber() {
        //give

        //when
        ChatBotResponse chatBotResponse = customerService.updateCustomerPhoneNumber(chatBotRequest);
        String responseMessage = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(responseMessage).isEqualTo("연락처 변경이 완료 되었습니다.");
    }

    @Test
    @Transactional
    void deleteCustomer() {
        //give

        //when
        ChatBotResponse chatBotResponse = customerService.deleteCustomer(chatBotRequest);
        String responseMessage = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(responseMessage).isEqualTo("회원탈퇴가 완료 되었습니다.");
    }

    @Test
    @Transactional
    void isCustomer() {
        //give

        //when
        boolean isCustomer = customerService.isCustomer(chatBotRequest);
        //then
        Assertions.assertThat(isCustomer).isTrue();
    }
}