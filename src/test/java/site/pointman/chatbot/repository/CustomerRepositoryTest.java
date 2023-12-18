package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.customer.Member;

import java.util.Optional;


@SpringBootTest

class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void save() {
        //give
        Member member = Member.builder()
                .userKey("테스트12")
                .name("테스트")
                .phone("01000000000")
                .build();
        //when
        customerRepository.save(member);

        //then
    }

    @Test
    @Transactional
    void findByCustomer() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        Optional<Member> byCustomer = customerRepository.findByCustomer(userKey);
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
        customerRepository.updateCustomerPhoneNumber(userKey,phoneNumber);

        //then
    }

    @Test
    @Transactional
    void delete() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        customerRepository.delete("QFJSyeIZbO77");

        //then
    }
}