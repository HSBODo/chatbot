package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.member.Member;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void save() {
        Member member = new Member(1L,
                "test",
                "관리자",
                "123456",
                "123456",
                "test@naver.com",
                "admin",
                "admin",
                "Y"
        );
        memberRepository.save(member.toEntity());
    }

    @Test
    void findAll() {
    }

    @Test
    void findByMember() {
        Optional<Member> byMember = memberRepository.findByMember("123");
        Assertions.assertThat(byMember).isNotEmpty();
    }
}