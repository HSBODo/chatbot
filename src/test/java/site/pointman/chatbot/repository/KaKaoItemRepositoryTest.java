package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

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
    void findByOrders() {
        String kakaoUserkey="QFERwysZbO77";
        List<Order> items = kaKaoItemRepository.findByOrders(kakaoUserkey);
        if(items.isEmpty()) throw new NullPointerException("주문이력이 없습니다.");
        items.stream()
                .forEach(item -> {
                    log.info("item={}",item.getItem_name());
                });
    }

    @Test
    void findByOrder(){
        Long orderId= 474872585L;
        String kakaoUserkey= "QFERwysZbO77";
        Optional<Order> maybeOrder = kaKaoItemRepository.findByOrder(kakaoUserkey, orderId);
        if(maybeOrder.isEmpty()) throw  new NullPointerException("주문번호와 일치하는 주문이 없습니다");
        Order order = maybeOrder.get();

        Long order_id = order.getOrder_id();
        log.info("orderId={}",order_id);
        Assertions.assertThat(order.getOrder_id()).isEqualTo(orderId);
    }

}