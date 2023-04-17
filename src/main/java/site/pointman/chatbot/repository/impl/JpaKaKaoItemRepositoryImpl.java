package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.kakaopay.KakaoPay;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KaKaoItemRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class JpaKaKaoItemRepositoryImpl implements KaKaoItemRepository {
    private final EntityManager em;

    public JpaKaKaoItemRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Item> findByDisplayItems() {
        return em.createQuery("select i from Item i where i.is_display='Y'", Item.class)
                .getResultList();
    }

    @Override
    public KakaoPay savePayReady(KakaoPay kakaoPay) {
        em.persist(kakaoPay);
        return kakaoPay;
    }

    @Override
    public void updatePayApprove(KakaoPay kakaoPay) {
        String kakaoUserkey = kakaoPay.getKakao_userkey();
        KakaoPay findOrder =em.createQuery("select i from KakaoPay i where i.status='ready' AND i.kakao_userkey='"+kakaoUserkey+"'", KakaoPay.class).getSingleResult();
        findOrder.setStatus(kakaoPay.getStatus());
        findOrder.setApproved_at(kakaoPay.getApproved_at());
        findOrder.setAid(kakaoPay.getAid());
        findOrder.setPayment_method_type(kakaoPay.getPayment_method_type());
    }

    @Override
    public Optional<KakaoPay> findByReadyOrder(String kakaoUserkey) {
        return Optional.ofNullable(em.createQuery("select i from KakaoPay i where i.status='ready' AND i.kakao_userkey='"+kakaoUserkey+"'", KakaoPay.class).getSingleResult());
    }
}
