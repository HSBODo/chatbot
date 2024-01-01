package site.pointman.chatbot.repository.customrepository.impl;

import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
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
    public Member updateMember(String userKey, Member member, boolean isUse) {
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getSingleResult();

        if (Objects.nonNull(member.getName())) findMember.changeName(member.getName());
        if (Objects.nonNull(member.getUserKey())) findMember.changeUserKey(member.getUserKey());
        if (Objects.nonNull(member.getPhoneNumber())) findMember.changePhoneNumber(member.getPhoneNumber());
        if (Objects.nonNull(member.getRole())) findMember.changeRole(member.getRole());

        return findMember;
    }



    @Override
    public Member updateMemberPhoneNumber(String userKey, String phoneNumber, boolean isUse) {
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getSingleResult();

        findMember.changePhoneNumber(phoneNumber);

        return findMember;
    }

    @Override
    public void delete(String userKey, boolean isUse) {
        Member removeMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getSingleResult();
        removeMember.delete();

        List<Product> removeProducts = em.createQuery("SELECT p FROM Product p JOIN Fetch p.productImages WHERE p.member.userKey=:userKey AND p.isUse = :isUse", Product.class)
                .setParameter("userKey", removeMember.getUserKey())
                .setParameter("isUse", isUse)
                .getResultList();

        removeProducts.forEach(removeProduct -> {
            removeProduct.getProductImages().delete();
        });

        int resultCount = em.createQuery("UPDATE Product p SET p.isUse = false WHERE p.member.userKey = :userKey")
                .setParameter("userKey", removeMember.getUserKey())
                .executeUpdate();
    }

}
