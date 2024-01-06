package site.pointman.chatbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository2;
import site.pointman.chatbot.repository.customrepository.impl.MemberCustomRepositoryImpl2;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String>, MemberCustomRepository {

    @Query("select new site.pointman.chatbot.domain.member.dto.MemberProfileDto (m.name, m.phoneNumber, m.role, m.createDate) from Member m where  m.isUse = :isUse")
    Page<MemberProfileDto>findAllMemberProfileDto(PageRequest pageRequest, @Param("isUse") boolean isUse);

    @Query("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse")
    Optional<Member> findByUserKey(@Param("userKey") String userKey, @Param("isUse") boolean isUse);

    @Query("select m from Member m where m.name=:name AND m.isUse = :isUse")
    Optional<Member> findByName(@Param("name") String name, @Param("isUse") boolean isUse);

    @Query("select new site.pointman.chatbot.domain.member.dto.MemberProfileDto (m.profile.nickname, m.phoneNumber, m.role, m.createDate) from Member m where m.userKey=:userKey AND m.isUse = :isUse")
    Optional<MemberProfileDto> findMemberProfileDtoByUserKey(@Param("userKey") String userKey, @Param("isUse") boolean isUse);

    @Query("select new site.pointman.chatbot.domain.member.dto.MemberProfileDto (m.name, m.phoneNumber, m.role, m.createDate) from Member m where m.name=:name AND m.isUse = :isUse")
    Optional<MemberProfileDto> findMemberProfileDtoByName(@Param("name") String name, @Param("isUse") boolean isUse);

    @Query("select new site.pointman.chatbot.domain.member.dto.MemberProfileDto (m.name, m.phoneNumber, m.role, m.createDate) from Member m where m.userKey=:userKey AND m.name=:name AND m.role=:role AND m.isUse = :isUse")
    Optional<MemberProfileDto> findMemberProfileByRole(@Param("name") String name, @Param("userKey") String userKey, @Param("role") MemberRole role, @Param("isUse") boolean isUse);

    @Query("select count(m) from Member m where m.userKey=:userKey AND m.isUse = :isUse")
    Integer findMemberCountByUserKey(@Param("userKey") String userKey, @Param("isUse") boolean isUse);
}