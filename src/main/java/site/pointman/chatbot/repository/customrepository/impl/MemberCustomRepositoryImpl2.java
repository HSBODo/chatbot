package site.pointman.chatbot.repository.customrepository.impl;


import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberCustomRepositoryImpl2 implements MemberCustomRepository2 {
    private boolean isUse = true;

    @PersistenceContext
    EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(String userKey) {
        Optional<Member> member = em.createQuery("SELECT m FROM Member m WHERE m.userKey =: userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getResultList()
                .stream()
                .findAny();

        return member;
    }

    public Optional<Member> findOneByName(String name) {
        Optional<Member> member = em.createQuery("SELECT m FROM Member m WHERE m.name =: name AND m.isUse = :isUse", Member.class)
                .setParameter("name", name)
                .setParameter("isUse", isUse)
                .getResultList()
                .stream()
                .findAny();
        return member;
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member WHERE m.isUse = :isUse")
                .setParameter("isUse",isUse)
                .getResultList();
    }

}
