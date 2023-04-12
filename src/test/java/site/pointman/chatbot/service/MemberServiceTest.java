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
        KakaoMember member = new KakaoMember();
        member.setKakaoUserkey("testuserkey1234zxc");
        member.setPartnerId("pointman");
        Map<String, String> result = memberService.join(member);
        assertThat(member).isEqualTo(result.get("member"));
    }

    @Test
    void saveLocation() {
        KakaoMemberLocation member = new KakaoMemberLocation();
        member.setKakaoUserkey("QFERwysZbO77"); //member 존재, 위치정보 있음
//        member.setKakaoUserkey("undefine");// member 존재, 위치정보 없음
//        member.setKakaoUserkey("QFERwysZbO771"); //member 존재, 위치정보 없음

        member.setX(BigDecimal.valueOf(37.4603776));
        member.setY(BigDecimal.valueOf(126.8187136));
        Map<String, String> result = memberService.saveLocation(member);
        String resultCode = String.valueOf(result.get("code"));
        log.info("result = {}",result);
        assertThat(resultCode).isEqualTo("0");
    }
}