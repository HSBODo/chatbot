package site.pointman.chatbot.repository.impl;

import org.springframework.stereotype.Repository;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.repository.KaKaoItemRepository;

import javax.persistence.EntityManager;
import java.util.List;
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
}
