package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import site.pointman.chatbot.dto.request.RequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class ProductServiceTest {
    @Autowired
    MockMvc mockMvc;
    @Test
    void getProducts() throws Exception {
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);

        mockMvc
                .perform(
                        post("/kakaochatbot/productsInfo") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }
}