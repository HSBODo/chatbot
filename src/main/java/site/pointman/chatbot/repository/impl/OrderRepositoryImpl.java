package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.OrderRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
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

    @Override
    public List<Order> findByUserKey(String userKey) {
        return em.createQuery("SELECT o FROM Order o WHERE o.buyerMember.userKey =:userKey",Order.class)
                .setParameter("userKey",userKey)
                .getResultList();
    }

    @Override
    public Optional<Order> findByProductId(Long productId, OrderStatus orderStatus) {
        return em.createQuery("SELECT o FROM Order o WHERE o.product.id =:productId AND o.status =:orderStatus",Order.class)
                .setParameter("productId",productId)
                .setParameter("orderStatus",orderStatus)
                .getResultList()
                .stream().findAny();
    }
}
