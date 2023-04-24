package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.KaKaoItemRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
public class JpaKaKaoItemRepositoryImpl implements KaKaoItemRepository {
    private final EntityManager em;

    public JpaKaKaoItemRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Item> findByDisplayItems() {
        return em.createQuery("select i from Item i where i.is_display=:display", Item.class)
                .setParameter("display","Y")
                .getResultList();
    }

    @Override
    public Optional<List<Order>> findByOrderItems(String kakaoUserkey) {
        return Optional.ofNullable(em.createQuery("select o from Order o where o.status='approve' AND o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getResultList());
    }

    @Override
    public Order savePayReady(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findByOrder(String kakaoUserkey, Long orderId) {
        Order findOrder = em.createQuery("select o from Order o where  o.order_id=:orderId AND o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("orderId",orderId)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getSingleResult();
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Item> findByItem(Long itemCode) {
        Item findItem = em.find(Item.class, itemCode);
        return Optional.ofNullable(findItem);
    }

    @Override
    public void updatePayApprove(Order order) {
        Long orderId = order.getOrder_id();
        Order findOrder = em.find(Order.class, orderId);
        findOrder.setStatus(order.getStatus());
        findOrder.setApproved_at(order.getApproved_at());
        findOrder.setAid(order.getAid());
        findOrder.setPayment_method_type(order.getPayment_method_type());
    }

    @Override
    public Optional<Order> findByReadyOrder(Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where o.status='ready' AND o.order_id='"+orderId+"'", Order.class).getSingleResult());
    }
}
