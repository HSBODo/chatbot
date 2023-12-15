package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.request.ChatBotRequest;


@SpringBootTest
@Slf4j
class LogServiceTest {

    @Autowired
    LogService logService;

    ObjectMapper mapper = new ObjectMapper();
    ChatBotRequest chatBotRequest;
    @BeforeEach
    public void setUp() throws JsonProcessingException {
        String body="{\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"intent\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\",\"extra\":{\"reason\":{\"code\":1,\"message\":\"OK\"}}},\"action\":{\"id\":\"657bc62306b53a111f7c354e\",\"name\":\"테스트_Json\",\"params\":{},\"detailParams\":{},\"clientExtra\":{}},\"userRequest\":{\"block\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\"},\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":true,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}},\"utterance\":\"테스트\",\"params\":{\"surface\":\"Kakaotalk.plusfriend\"},\"lang\":\"ko\",\"timezone\":\"Asia/Seoul\"},\"contexts\":[]}\n";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue(body, ChatBotRequest.class);
    }

    @Test
    void insert() throws Exception {
        logService.insert(chatBotRequest);
    }

    @Test
    void testInsert() {
    }
}