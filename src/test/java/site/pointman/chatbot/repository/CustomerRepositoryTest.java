package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
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
        String userKey = "QFJSyeIZbO77";
        Optional<Customer> byCustomer = customerRepository.findByCustomer(userKey);
        Customer customer = byCustomer.get();
        List<Product> products = customer.getProducts();
        System.out.println("products = " + products.get(0).getId());

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