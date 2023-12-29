package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String>, MemberCustomRepository {

    @Query("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse")
    Optional<Member> findByUserKey(@Param("userKey") String userKey, @Param("isUse") boolean isUse);

    @Query("select m from Member m where m.name=:name AND m.isUse = :isUse")
    Optional<Member> findByName(@Param("name") String name, @Param("isUse") boolean isUse);

    @Query("select m from Member m where m.userKey=:userKey AND m.name=:name AND m.role=:role AND m.isUse = :isUse")
    Optional<Member> findByRole(@Param("name") String name, @Param("userKey") String userKey, @Param("role") MemberRole role, @Param("isUse") boolean isUse);
}