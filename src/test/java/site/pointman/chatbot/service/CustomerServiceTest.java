package site.pointman.chatbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.request.ChatBotRequest;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    CustomerService customerService;

    @Test
    void join() {
        ChatBotRequest chatBotRequest = new ChatBotRequest();
        customerService.join(chatBotRequest);
    }
}