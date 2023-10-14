package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.order.Order;

import java.util.Optional;

public interface CustomerRepository {
    void insertCustomer(Customer customer);

    Optional<Customer> findByCustomer(String userKey, String isUse);
}
