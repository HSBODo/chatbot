package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.customer.Member;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.repository.CustomerRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {
    private final EntityManager em;

    public CustomerRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findByCustomer(String userKey) {
        List<Member> members = em.createQuery("select c from Customer c where c.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getResultList();
        return members.stream().findAny();
    }

    @Override
    public void updateCustomerPhoneNumber(String userKey, String phoneNumber) {
        Member findMember = em.createQuery("select c from Customer c where c.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getSingleResult();
        findMember.changePhone(phoneNumber);
    }

    @Override
    public void delete(String userKey) {
        Member findMember = em.createQuery("select c from Customer c where c.userKey=:userKey", Member.class)
                .setParameter("userKey", userKey)
                .getSingleResult();

        List<Product> findProducts = em.createQuery("select p from Product p where p.customer.userKey=:userKey", Product.class)
                .setParameter("userKey", userKey)
                .getResultList();

        findProducts.forEach(product -> {
            Long productImageId = product.getProductImages().getId();
            ProductImage findProductImage = em.createQuery("select p from ProductImage p where p.id=:id", ProductImage.class)
                    .setParameter("id", productImageId)
                    .getSingleResult();
            em.remove(product);
            em.remove(findProductImage);
        });

        em.remove(findMember);
    }
}
