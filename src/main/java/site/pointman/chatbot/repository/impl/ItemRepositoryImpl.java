package site.pointman.chatbot.repository.impl;


import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.repository.ItemRepository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class ItemRepositoryImpl implements ItemRepository {

    private final EntityManager em;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Item> findByDisplayItems() {
        return em.createQuery("select i from Item i where i.is_display=:display", Item.class)
                .setParameter("display","Y")
                .getResultList();
    }
    @Override
    public Optional<Item> findByItem(Long itemCode) {
        Item findItem = em.find(Item.class, itemCode);
        return Optional.ofNullable(findItem);
    }

    @Override
    public Optional<Item> updateMinusItemQuantity(Long itemCode, Long quantity) {
        Item findItem = em.find(Item.class, itemCode);
        long total = findItem.getTotal_quantity() - quantity;
        findItem.changeQuantity(total);
        return Optional.ofNullable(findItem);
    }
}
