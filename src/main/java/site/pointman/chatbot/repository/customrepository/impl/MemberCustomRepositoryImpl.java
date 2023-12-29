package site.pointman.chatbot.repository.customrepository.impl;

import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.repository.customrepository.MemberCustomRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
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

        List<Product> removeProducts = em.createQuery("select p from Product p where p.member.userKey=:userKey AND p.isUse = :isUse", Product.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getResultList();

        removeProducts.forEach(removeProduct -> {
            Long productImageId = removeProduct.getProductImages().getId();

            ProductImage removeProductImage = em.createQuery("select p from ProductImage p where p.id=:id AND p.isUse = :isUse", ProductImage.class)
                    .setParameter("id", productImageId)
                    .setParameter("isUse", isUse)
                    .getSingleResult();

            removeProductImage.delete();
            removeProduct.delete();
        });

        removeMember.delete();
    }

}
