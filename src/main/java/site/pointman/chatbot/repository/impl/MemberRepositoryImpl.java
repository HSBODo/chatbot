package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
public class MemberRepositoryImpl implements MemberRepository {
    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findByUserKey(String userKey) {
        return em.createQuery("select m from Member m where m.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
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
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getSingleResult();
        if (Objects.nonNull(member.getName())) findMember.changeName(member.getName());
        if (Objects.nonNull(member.getUserKey())) findMember.changeUserKey(member.getUserKey());
        if (Objects.nonNull(member.getPhoneNumber())) findMember.changePhoneNumber(member.getPhoneNumber());
        if (Objects.nonNull(member.getRole())) findMember.changeRole(member.getRole());
    }

    @Override
    public void updateMemberPhoneNumber(String userKey, String phoneNumber) {
        Member findMember = em.createQuery("select m from Member m where m.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getSingleResult();
        findMember.changePhoneNumber(phoneNumber);
    }

    @Override
    public void delete(String userKey) {
        Member removeMember = em.createQuery("select m from Member m where m.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getSingleResult();

        List<Product> removeProducts = em.createQuery("select p from Product p where p.member.userKey=:userKey", Product.class)
                .setParameter("userKey", userKey)
                .getResultList();

        removeProducts.forEach(removeProduct -> {
            Long productImageId = removeProduct.getProductImages().getId();

            ProductImage removeProductImage = em.createQuery("select p from ProductImage p where p.id=:id", ProductImage.class)
                    .setParameter("id", productImageId)
                    .getSingleResult();

            removeProductImage.delete();
            removeProduct.delete();
        });

        removeMember.delete();
    }
}
