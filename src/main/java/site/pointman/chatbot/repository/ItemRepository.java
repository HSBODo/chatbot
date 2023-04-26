package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> findByDisplayItems();
    Optional<Item> findByItem(Long itemCode);

}
