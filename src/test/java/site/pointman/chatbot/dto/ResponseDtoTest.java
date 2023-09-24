package site.pointman.chatbot.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import site.pointman.chatbot.dto.request.RequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
class ResponseDtoTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Response SimpleText")
    void SimpleText() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);
        String expectBySimpleText = "$.template.outputs[0].simpleText.text";
        mockMvc
                .perform(
                        post("/test/simpleText") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
                .andExpect(jsonPath("version").value("2.0"))
                .andExpect(jsonPath(expectBySimpleText).value("심플텍스트 테스트"));
    }
    @Test
    @DisplayName("Response SimpleImage")
    void SimpleImage() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);
        String expectBySimpleImgUrl = "$.template.outputs[0].simpleImage.imageUrl";
        String expectBySimpleAltText = "$.template.outputs[0].simpleImage.altText";
        mockMvc
                .perform(
                        post("/test/simpleImg") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
                .andExpect(jsonPath("version").value("2.0"))
                .andExpect(jsonPath(expectBySimpleImgUrl).value("https://www.youtube.com/"))
                .andExpect(jsonPath(expectBySimpleAltText).value("유튜브 이미지"))
                ;
    }

    @Test
    @DisplayName("Response TextCard")
    void TextCard() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);
        String expectByTextCardText = "$.template.outputs[0].textCard.text";
        String expectByTextCardButton = "$.template.outputs[0].textCard.buttons";
        mockMvc
                .perform(
                        post("/test/textCard") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
                .andExpect(jsonPath("version").value("2.0"))
                .andExpect(jsonPath(expectByTextCardText).value("텍스트카드 테스트"))
                .andExpect(jsonPath(expectByTextCardButton).isNotEmpty())
                ;
    }

    @Test
    @DisplayName("Response BasicCard")
    void BasicCard() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);
        String expectByBasicCardTitle = "$.template.outputs[0].basicCard.title";
        String expectByBasicCardDescription = "$.template.outputs[0].basicCard.description";
        String expectByBasicCardThumbnail = "$.template.outputs[0].basicCard.thumbnail.imageUrl";
        String expectByTextCardButton = "$.template.outputs[0].basicCard.buttons";
        mockMvc
                .perform(
                        post("/test/basicCard") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
                .andExpect(jsonPath("version").value("2.0"))
                .andExpect(jsonPath(expectByBasicCardTitle).value("제목"))
                .andExpect(jsonPath(expectByBasicCardDescription).value("설명"))
                .andExpect(jsonPath(expectByBasicCardThumbnail).value("섬네일 URL https://www.youtube.com/"))
                .andExpect(jsonPath(expectByTextCardButton).isNotEmpty())
        ;
    }

    @Test
    @DisplayName("Response CommerceCard")
    void CommerceCard() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);

        mockMvc
                .perform(
                        post("/test/commerceCard") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }

    @Test
    @DisplayName("Response ListCard")
    void ListCard() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);

        mockMvc
                .perform(
                        post("/test/listCard") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }

    @Test
    @DisplayName("Response Carousel")
    void Carousel() throws Exception{
        RequestDto requestDto = new RequestDto();
        String content = new ObjectMapper().writeValueAsString(requestDto);

        mockMvc
                .perform(
                        post("/test/carousel") // url
                                .contentType(MediaType.APPLICATION_JSON) // contentType 설정
                                .content(content) // body 데이터
                )
                .andDo(print()) // api 수행내역 로그 출력
                .andExpect(status().isOk()) // response status 200 검증
        ;
    }
}