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
    public void insert(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Optional<Customer> findByCustomer(String userKey) {
        List<Customer> customers = em.createQuery("select c from Customer c where c.userKey=:userKey", Customer.class)
                .setParameter("userKey", userKey)
                .getResultList();
        return customers.stream().findAny();
    }

    @Override
    public void updateCustomerPhoneNumber(String userKey, String phoneNumber) {
        Customer findCustomer = em.createQuery("select c from Customer c where c.userKey=:userKey", Customer.class)
                .setParameter("userKey", userKey)
                .getSingleResult();
        findCustomer.changePhone(phoneNumber);
    }

    @Override
    public void delete(String userKey) {
        Customer findCustomer = em.createQuery("select c from Customer c where c.userKey=:userKey", Customer.class)
                .setParameter("userKey", userKey)
                .getSingleResult();
        em.remove(findCustomer);
    }
}
