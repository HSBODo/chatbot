package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository2 {

    void save(Member member);

    Optional<Member> findOne(String userKey);

    Optional<Member> findOneByName(String name);

    List<Member> findAll();

}
