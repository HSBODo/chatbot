package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.repository.ItemRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final EntityManager em;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Order> findByApproveOrder(Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where o.status=:status AND o.order_id=:orderId", Order.class)
                .setParameter("status",OrderStatus.결제승인)
                .setParameter("orderId",orderId)
                .getSingleResult());
    }

    @Override
    public List<Item> findByDisplayItems() {
        return em.createQuery("select i from Item i where i.is_display=:display", Item.class)
                .setParameter("display","Y")
                .getResultList();
    }

    @Override
    public List<Order> findByApproveOrders(String kakaoUserkey) {
        return em.createQuery("select o from Order o where o.status = :status and o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("status",OrderStatus.결제승인)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getResultList()
                ;
    }

    @Override
    public Order savePayReady(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findByOrder(String kakaoUserkey, Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where  o.order_id=:orderId AND o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("orderId",orderId)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getSingleResult());
    }

    @Override
    public Optional<Item> findByItem(Long itemCode) {
        Item findItem = em.find(Item.class, itemCode);
        return Optional.ofNullable(findItem);
    }

    @Override
    public Optional<Order> updatePayApprove(Long orderId,Order updateParams) {
        Order findOrder = em.find(Order.class, orderId);
        findOrder.changeStatus(updateParams.getStatus());
        findOrder.changeApprovedAt(updateParams.getApproved_at());
        findOrder.changeAid(updateParams.getAid());
        findOrder.changePayMethod(updateParams.getPayment_method_type());
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> updatePayCancel(Long orderId,Order updateParams) {
        Order findOrder = em.find(Order.class, orderId);
        findOrder.changeStatus(updateParams.getStatus());
        findOrder.changeCancelAt(updateParams.getCanceled_at());
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findByReadyOrder(Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where o.status=:status AND o.order_id=:orderId", Order.class)
                .setParameter("status", OrderStatus.결제대기)
                .setParameter("orderId",orderId)
                .getSingleResult()
        );
    }
}
