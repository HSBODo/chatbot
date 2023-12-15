package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.customer.Customer;

import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findByCustomer(String userKey);
    void updateCustomerPhoneNumber(String userKey, String phoneNumber);
    void delete(String userKey);

}
