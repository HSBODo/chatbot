package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.customer.Customer;
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
    public void insertCustomer(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Optional<Customer> findByCustomer(String userKey, String isUse) {
        List<Customer> userKeys = em.createQuery("select c from Customer c where c.userKey=:userKey AND c.isUse = :isUse", Customer.class)
                .setParameter("userKey", userKey)
                .setParameter("isUse", isUse)
                .getResultList();
        return userKeys.stream().findAny();
    }
}
