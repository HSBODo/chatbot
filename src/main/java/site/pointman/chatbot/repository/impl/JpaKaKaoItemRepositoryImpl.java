package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.kakaopay.KakaoPay;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
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
    public List<KakaoPay> findByOrderItems(String kakaoUserkey) {
        return em.createQuery("select o from KakaoPay o where o.status='approve' AND o.kakao_userkey=:kakaoUserkey", KakaoPay.class)
                .setParameter("kakaoUserkey",kakaoUserkey)
                .getResultList();
    }

    @Override
    public KakaoPay savePayReady(KakaoPay kakaoPay) {
        em.persist(kakaoPay);
        return kakaoPay;
    }

    @Override
    public Item findByItem(Long itemCode) {
        Item findItem = em.find(Item.class, itemCode);
        return findItem;
    }

    @Override
    public void updatePayApprove(KakaoPay kakaoPay) {
        Long orderId = kakaoPay.getOrder_id();
        KakaoPay findOrder = em.find(KakaoPay.class, orderId);
        findOrder.setStatus(kakaoPay.getStatus());
        findOrder.setApproved_at(kakaoPay.getApproved_at());
        findOrder.setAid(kakaoPay.getAid());
        findOrder.setPayment_method_type(kakaoPay.getPayment_method_type());
        log.info("update");
    }

    @Override
    public Optional<KakaoPay> findByReadyOrder(Long orderId) {
        return Optional.ofNullable(em.createQuery("select i from KakaoPay i where i.status='ready' AND i.order_id='"+orderId+"'", KakaoPay.class).getSingleResult());
    }
}
