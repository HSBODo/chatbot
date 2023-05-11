package site.pointman.chatbot.repository.impl;

import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
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
    public List<Order> findByApproveOrders(String kakaoUserkey) {
        return em.createQuery("select o from Order o where o.status = :status and o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("status", OrderStatus.결제승인)
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
    public Optional<Order> findByReadyOrder(Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where o.status=:status AND o.order_id=:orderId", Order.class)
                .setParameter("status", OrderStatus.결제대기)
                .setParameter("orderId",orderId)
                .getSingleResult()
        );
    }

    @Override
    public Optional<Order> findByApproveOrder(Long orderId) {
        return em.createQuery("select o from Order o where o.status=:status AND o.order_id=:orderId", Order.class)
                .setParameter("status",OrderStatus.결제승인)
                .setParameter("orderId",orderId)
                .getResultList().stream().findAny();
    }

    @Override
    public Optional<Order> findByOrder(String kakaoUserkey, Long orderId) {
        return Optional.ofNullable(em.createQuery("select o from Order o where  o.order_id=:orderId AND o.kakao_userkey=:kakaoUserkey", Order.class)
                .setParameter("orderId",orderId)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getSingleResult());
    }

    @Override
    public Optional<Order> updatePayApprove(Order updateParams) {
        Order findOrder = em.find(Order.class, updateParams.getOrder_id());
        findOrder.changeStatus(updateParams.getStatus());
        findOrder.changeApprovedAt(updateParams.getApproved_at());
        findOrder.changeAid(updateParams.getAid());
        findOrder.changePayMethod(updateParams.getPayment_method_type());
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> updatePayCancel(Order updateParams) {
        Order findOrder = em.find(Order.class, updateParams.getOrder_id());
        findOrder.changeStatus(updateParams.getStatus());
        findOrder.changeCancelAt(updateParams.getCanceled_at());
        return Optional.ofNullable(findOrder);
    }

    @Override
    public List<Order> findBySalesRank() {
        List<Order> orderList = em.createQuery("select SUM(o.quantity) as quantity, o.item_code from Order o where  o.status=:status group by o.item_code order by o.item_code desc", Order.class)
                .setParameter("status", OrderStatus.결제승인)
                .getResultList();
        return orderList;
    }
}
