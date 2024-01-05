package site.pointman.chatbot.controller.kakaochatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    private String body;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        body="{\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"intent\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\",\"extra\":{\"reason\":{\"code\":1,\"message\":\"OK\"}}},\"action\":{\"id\":\"657bc62306b53a111f7c354e\",\"name\":\"테스트_Json\",\"params\":{},\"detailParams\":{},\"clientExtra\":{}},\"userRequest\":{\"block\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\"},\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":true,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}},\"utterance\":\"테스트\",\"params\":{\"surface\":\"Kakaotalk.plusfriend\"},\"lang\":\"ko\",\"timezone\":\"Asia/Seoul\"},\"contexts\":[]}\n";
    }

    @Test
    void join() throws Exception {
        mockMvc
                .perform(
                        post("/kakaochatbot/customer/POST/join") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(body) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }

    @Test
    void profile() throws Exception {

        mockMvc
                .perform(
                        post("/kakaochatbot/customer/GET/profile") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(body) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }

    @Test
    void updatePhoneNumber() throws Exception {
        mockMvc
                .perform(
                        post("/kakaochatbot/customer/PATCH/PhoneNumber") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(body) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }

    @Test
    void delete() throws Exception {
        mockMvc
                .perform(
                        post("/kakaochatbot/customer/DELETE") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(body) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }
}