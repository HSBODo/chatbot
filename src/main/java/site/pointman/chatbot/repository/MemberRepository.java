package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<Member> findByCustomer(String userKey);
    void updateCustomerPhoneNumber(String userKey, String phoneNumber);
    void delete(String userKey);

}
