package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.OrderRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

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

    @Override
    public Optional<Order> findByOrderId(Long orderId) {
        return em.createQuery("SELECT o FROM Order o WHERE o.orderId =:orderId",Order.class)
                .setParameter("orderId",orderId)
                .getResultList()
                .stream().findAny();
    }
}
