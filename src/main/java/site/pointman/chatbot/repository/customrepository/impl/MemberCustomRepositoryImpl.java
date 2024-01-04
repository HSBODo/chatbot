package site.pointman.chatbot.repository.customrepository.impl;

import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional
@Slf4j
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final EntityManager em;

    public MemberCustomRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member updateMember(String name, MemberProfileDto memberDto, boolean isUse) {
        Member findMember = em.createQuery("select m from Member m where m.name=:name AND m.isUse = :isUse", Member.class)
                .setParameter("name", name)
                .setParameter("isUse", isUse)
                .getSingleResult();

        if (Objects.nonNull(memberDto.getName())) findMember.changeName(memberDto.getName());
        if (Objects.nonNull(memberDto.getPhoneNumber())) findMember.changePhoneNumber(memberDto.getPhoneNumber());
        if (Objects.nonNull(memberDto.getRole())) findMember.changeRole(memberDto.getRole());

        return findMember;
    }

    @Override
    public void delete(String userKey) {
        Member removeMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getSingleResult();
        removeMember.delete();

        List<Product> removeProducts = em.createQuery("SELECT p FROM Product p JOIN Fetch p.productImages WHERE p.member.userKey=:userKey AND p.isUse = :isUse", Product.class)
                .setParameter("userKey", removeMember.getUserKey())
                .setParameter("isUse", true)
                .getResultList();

        removeProducts.forEach(removeProduct -> {
            removeProduct.getProductImages().delete();
        });

        em.createQuery("UPDATE Product p SET p.isUse = :isUse WHERE p.member.userKey = :userKey")
                .setParameter("userKey", removeMember.getUserKey())
                .setParameter("isUse",false)
                .executeUpdate();
    }

}
