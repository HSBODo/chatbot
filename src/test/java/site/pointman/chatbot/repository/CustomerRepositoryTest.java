package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.customer.Customer;

import java.util.Optional;


@SpringBootTest
@Transactional
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void save() {
        //give
        Customer customer = Customer.builder()
                .userKey("테스트12")
                .name("테스트")
                .phone("01000000000")
                .build();
        //when
        customerRepository.save(customer);

        //then
    }

    @Test
    void findByCustomer() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        Optional<Customer> byCustomer = customerRepository.findByCustomer(userKey);
        Customer customer = byCustomer.get();

        //then
        Assertions.assertThat(customer.getUserKey()).isEqualTo(userKey);
    }

    @Test
    void updateCustomerPhoneNumber() {
        //give
        String userKey = "QFJSyeIZbO77";
        String phoneNumber = "01011112222";
        //when
        customerRepository.updateCustomerPhoneNumber(userKey,phoneNumber);

        //then
    }

    @Test
    void delete() {
        //give
        String userKey = "QFJSyeIZbO77";

        //when
        customerRepository.delete("QFJSyeIZbO77");

        //then
    }
}