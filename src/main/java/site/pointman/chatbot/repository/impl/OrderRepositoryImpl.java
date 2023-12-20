package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.OrderRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class OrderRepositoryImpl implements OrderRepository {
    private final EntityManager em;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Order order) {
        em.persist(order);
        return order.getOrderId();
    }
}
