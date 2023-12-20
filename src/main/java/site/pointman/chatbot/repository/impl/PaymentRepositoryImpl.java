package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.repository.PaymentRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {
    private final EntityManager em;

    public PaymentRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public PaymentInfo save(PaymentInfo paymentInfo) {
        em.persist(paymentInfo);
        return paymentInfo;
    }

    @Override
    public Optional<PaymentInfo> findByPaymentReadyStatus(Long orderId) {
        return Optional.ofNullable(em.createQuery("select p from PaymentInfo p where p.status=:status AND p.orderId=:orderId", PaymentInfo.class)
                .setParameter("status", PaymentStatus.결제준비)
                .setParameter("orderId",orderId)
                .getSingleResult()
        );
    }

    @Override
    public Optional<PaymentInfo> findByPaymentSuccessStatus(Long orderId) {
        return em.createQuery("select p from PaymentInfo p where p.status=:status AND p.orderId=:orderId", PaymentInfo.class)
                .setParameter("status", PaymentStatus.결제완료)
                .setParameter("orderId",orderId)
                .getResultList().stream().findAny();
    }
}
