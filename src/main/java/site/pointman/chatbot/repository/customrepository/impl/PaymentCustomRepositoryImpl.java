package site.pointman.chatbot.repository.customrepository.impl;

import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.repository.customrepository.PaymentCustomRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {
    private final EntityManager em;

    public PaymentCustomRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<PaymentInfo> findByPaymentStatus(Long orderId, PaymentStatus status) {
        return em.createQuery("select p " +
                        "from PaymentInfo p " +
                        "join fetch p.buyerMember " +
                        "join fetch p.product " +
                        "where p.status=:status " +
                        "AND " +
                        "p.orderId=:orderId", PaymentInfo.class)
                .setParameter("status", status)
                .setParameter("orderId",orderId)
                .getResultList().stream().findAny();
    }

    @Override
    public Optional<PaymentInfo> findByOrderId(Long orderId) {
        return em.createQuery("select p " +
                        "from PaymentInfo p " +
                        "join fetch p.buyerMember " +
                        "join fetch p.product " +
                        "where p.orderId=:orderId", PaymentInfo.class)
                .setParameter("orderId",orderId)
                .getResultList().stream().findAny();
    }
}
