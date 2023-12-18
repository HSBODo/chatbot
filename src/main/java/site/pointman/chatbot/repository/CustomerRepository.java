package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.customer.Member;

import java.util.Optional;

public interface CustomerRepository {
    void save(Member member);
    Optional<Member> findByCustomer(String userKey);
    void updateCustomerPhoneNumber(String userKey, String phoneNumber);
    void delete(String userKey);

}
