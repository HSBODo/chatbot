package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.customer.Customer;

import java.util.Optional;


@SpringBootTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void insertCustomer() {
        Customer customer = Customer.builder()
                .userKey("테스트12")
                .name("테스트")
                .phone("01000000000")
                .build();
                customerRepository.insert(customer);
    }

    @Test
    void findByCustomer() {
        String userKey = "테스트12";
        Optional<Customer> byCustomer = customerRepository.findByCustomer(userKey);
        if(byCustomer.isEmpty()){
            Assertions.assertThat(byCustomer.orElse(null)).isNull();
        }else{
            Customer customer = byCustomer.get();
            Assertions.assertThat(customer.getUserKey()).isEqualTo(userKey);
        }

    }

    @Test
    void updateCustomerPhoneNumber() {
        customerRepository.updateCustomerPhoneNumber("QFJSyeIZbO77","01011112222");
    }

    @Test
    void deleteCustomer() {
        customerRepository.delete("QFJSyeIZbO77");
    }
}