package site.pointman.chatbot.repository.impl;


import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberRepositoryImpl implements MemberRepository {
    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Optional<Member> findByMember(String id) {
        Member findMember = em.find(Member.class,id);
        return Optional.ofNullable(findMember);
    }
}
