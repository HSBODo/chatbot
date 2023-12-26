package site.pointman.chatbot.repository.custom;

import site.pointman.chatbot.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository {
    List<Member> findByAll();
    Optional<Member> findByUserKey(String userKey);
    Optional<Member> findAdmin(String name, String userKey);
    Optional<Member> findByName(String name);
    void updateMember(String userKey, Member member);
    void updateMemberPhoneNumber(String userKey, String phoneNumber);
    void delete(String userKey);

}
