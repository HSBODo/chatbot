package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.domain.item.ItemOptionCategory;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);
    List<Item> findByDisplayItems();
    Optional<Item> findByItem(Long itemCode);
    Optional<Item> updateItemQuantityMinus(Long itemCode,Long quantity);
    Optional<Item> updateItemQuantityPlus(Long itemCode,Long quantity);

    List<ItemOption> findByItemOptions(Long itemCode, ItemOptionCategory itemOptionCategory);

    Optional<ItemOption> findByItemOption(Long optionCode);
    ItemOption saveItemOption(ItemOption itemOption);

}
