package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepositoryImpl implements MemberRepository {
    private final EntityManager em;

    public JpaMemberRepositoryImpl(EntityManager em) {
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
