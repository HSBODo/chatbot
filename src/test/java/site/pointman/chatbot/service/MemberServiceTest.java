package site.pointman.chatbot.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Test
    void isKakaoMember() {
        String kakaoUserkey="123123";
        boolean kakaoMember = memberService.isKakaoMember(kakaoUserkey);
        Assertions.assertThat(kakaoMember).isFalse();
    }

    @Test
    void join() {
    }

    @Test
    void saveLocation() {
    }

    @Test
    void saveAttribute() {
    }

    @Test
    void updateAttribute() {
    }
}