package site.pointman.chatbot.service;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Test
    void join() {


    }

    @Test
    void saveLocation() {
        KakaoMemberLocation memberLocation = KakaoMemberLocation.builder()
                .kakaoUserkey("QFERwysZbO77")
                .x(BigDecimal.valueOf(37.4603776))
                .y(BigDecimal.valueOf(126.8187136))
                .build();
        Map<String, String> result = memberService.saveLocation(memberLocation);
        String resultCode = String.valueOf(result.get("code"));
        log.info("result = {}",result);
        assertThat(resultCode).isEqualTo("0");
    }
}