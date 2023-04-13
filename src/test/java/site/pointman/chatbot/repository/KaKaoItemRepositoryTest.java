package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.repository.impl.JpaKaKaoItemRepositoryImpl;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class KaKaoItemRepositoryTest {
    @Autowired
    private KaKaoItemRepository kaKaoItemRepository;

    @Test
    void findByDisplayItems() {
        List<Item> items = kaKaoItemRepository.findByDisplayItems();
        items.stream()
                .forEach(item -> {
                    log.info("item={}",item.getItemCode());
                });
    }
}