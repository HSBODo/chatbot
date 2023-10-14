package site.pointman.chatbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.request.RequestDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {
    @Autowired
    CustomerService customerService;

    @Test
    void join() {
        RequestDto requestDto = new RequestDto();
        customerService.join(requestDto);
    }
}