package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;

public interface MemberRepository extends JpaRepository<Member,String>, MemberCustomRepository {

}
