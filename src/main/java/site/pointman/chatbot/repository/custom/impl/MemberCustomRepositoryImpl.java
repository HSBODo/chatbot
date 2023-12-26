package site.pointman.chatbot.repository.custom.impl;

import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.repository.custom.MemberCustomRepository;

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
    public Optional<Member> findByUserKey(String userKey) {
        return em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getResultList().stream().findAny();
    }

    @Override
    public List<Member> findByAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList().stream().findAny();
    }

    @Override
    public void updateMember(String userKey, Member member) {
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getSingleResult();
        if (Objects.nonNull(member.getName())) findMember.changeName(member.getName());
        if (Objects.nonNull(member.getUserKey())) findMember.changeUserKey(member.getUserKey());
        if (Objects.nonNull(member.getPhoneNumber())) findMember.changePhoneNumber(member.getPhoneNumber());
        if (Objects.nonNull(member.getRole())) findMember.changeRole(member.getRole());
    }

    @Override
    public void updateMemberPhoneNumber(String userKey, String phoneNumber) {
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getSingleResult();
        findMember.changePhoneNumber(phoneNumber);
    }

    @Override
    public void delete(String userKey) {
        Member removeMember = em.createQuery("select m from Member m where m.userKey=:userKey AND m.isUse = :isUse", Member.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getSingleResult();

        List<Product> removeProducts = em.createQuery("select p from Product p where p.member.userKey=:userKey AND p.isUse = :isUse", Product.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", true)
                .getResultList();

        removeProducts.forEach(removeProduct -> {
            Long productImageId = removeProduct.getProductImages().getId();

            ProductImage removeProductImage = em.createQuery("select p from ProductImage p where p.id=:id AND p.isUse = :isUse", ProductImage.class)
                    .setParameter("id", productImageId)
                    .setParameter("isUse", true)
                    .getSingleResult();

            removeProductImage.delete();
            removeProduct.delete();
        });

        removeMember.delete();
    }

    @Override
    public Optional<Member> findAdmin(String name, String userKey) {
        return em.createQuery("select m from Member m where m.userKey=:userKey AND m.name=:name AND m.role=:role AND m.isUse = :isUse", Member.class)
                .setParameter("name", name)
                .setParameter("userKey", userKey)
                .setParameter("role", MemberRole.ADMIN)
                .setParameter("isUse", true)
                .getResultList().stream().findAny();
    }
}
