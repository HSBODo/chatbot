package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;

import java.util.Optional;


@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void save() {
        //give
        Member member = Member.builder()
                .userKey("테스트12")
                .name("테스트")
                .phoneNumber("01000000000")
                .build();
        //when
        memberRepository.save(member);

        //then
    }

    @Test
    @Transactional
    void findByCustomer() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        Optional<Member> byCustomer = memberRepository.findByCustomer(userKey);
        Member member = byCustomer.get();

        //then
        Assertions.assertThat(member.getUserKey()).isEqualTo(userKey);
    }

    @Test
    @Transactional
    void updateCustomerPhoneNumber() {
        //give
        String userKey = "QFJSyeIZbO77";
        String phoneNumber = "01011112222";
        //when
        memberRepository.updateCustomerPhoneNumber(userKey,phoneNumber);

        //then
    }

    @Test
    @Transactional
    void delete() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        memberRepository.delete("QFJSyeIZbO77");

        //then
    }
}