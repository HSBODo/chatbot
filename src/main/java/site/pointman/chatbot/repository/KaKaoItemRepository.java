package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;

import java.util.List;

public interface KaKaoItemRepository {
    List<Item> findByDisplayItems();
}
