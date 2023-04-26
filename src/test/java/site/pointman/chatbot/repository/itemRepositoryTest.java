package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class itemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByDisplayItems() {
        List<Item> items = itemRepository.findByDisplayItems();
        if(items.isEmpty()) throw new NullPointerException("display 가능한 아이템이 없습니다.");
        items.stream()
                .forEach(item -> {
                    log.info("isDisplay={}",item.getIs_display());
                    Assertions.assertThat(item.getIs_display()).isEqualTo("Y");
                });
    }

    @Test
    void findByItem(){
        Long itemCode = 1L;
        Optional<Item> maybeItem = itemRepository.findByItem(itemCode);
        if (maybeItem.isEmpty()) throw new NullPointerException("아이템 코드와 일치하는 상품 없음");
        Item item = maybeItem.get();
        Assertions.assertThat(item.getItemCode()).isEqualTo(itemCode);

    }












}