package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;

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

    @Test
    void findByOrderItems() {
        String kakaoUserkey="QFERwysZbO77";
        List<Order> items = kaKaoItemRepository.findByOrderItems(kakaoUserkey);
        items.stream()
                .forEach(item -> {
                    log.info("item={}",item.getItem_code());
                });
    }

    @Test
    void findByOrder(){
        Long orderId= 474872585L;
        String kakaoUserkey= "QFERwysZbO77";
        Order order = kaKaoItemRepository.findByOrder(kakaoUserkey,orderId).get();
        Long order_id = order.getOrder_id();
        log.info("orderId={}",order_id);
        Assertions.assertThat(order.getOrder_id()).isEqualTo(orderId);
    }

}