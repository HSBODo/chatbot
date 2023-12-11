package site.pointman.chatbot.repository;

import site.pointman.chatbot.service.domain.customer.Customer;

import java.util.Optional;

public interface CustomerRepository {
    void insertCustomer(Customer customer);

    Optional<Customer> findByCustomer(String userKey, String isUse);
}
