package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.kakaopay.KakaoPay;

import java.util.List;
import java.util.Optional;

public interface KaKaoItemRepository {
    List<Item> findByDisplayItems();

    Item findByItem(Long itemCode);
    KakaoPay savePayReady(KakaoPay kakaoPay);
    Optional<KakaoPay> findByReadyOrder(Long orderId);
    void updatePayApprove(KakaoPay kakaoPay);
}
