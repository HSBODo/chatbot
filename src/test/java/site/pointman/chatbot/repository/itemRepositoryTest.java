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
    void findByOrder() {
        String kakaoUserkey="QFERwysZbO77";
        Long orderId= 216960794L;
        Optional<Order> maybeOrder = itemRepository.findByOrder(kakaoUserkey, orderId);
        if(maybeOrder.isEmpty()) throw new NullPointerException("주문이력이 없습니다.");
        Order order = maybeOrder.get();
        Assertions.assertThat(order.getOrder_id()).isEqualTo(orderId);
    }

    @Test
    void findByApproveOrders(){
        String kakaoUserkey="QFERwysZbO77";
        List<Order> result = itemRepository.findByApproveOrders(kakaoUserkey);
        if(result.isEmpty()) throw new NullPointerException("주문내역없음");
        result.stream().forEach(order -> {
            Assertions.assertThat(order.getKakao_userkey()).isEqualTo(kakaoUserkey);
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

    @Test
    void savePayReady(){
        Order readyOrder = Order.builder()
                .cid("cid")
                .tid("tid")
                .kakao_userkey("kakaoUserky")
                .order_id(1231231132L)
                .item_code(1232132213L)
                .item_name("테스트")
                .quantity(1)
                .total_amount(3000)
                .vat_amount(0)
                .tax_free_amount(0)
                .status(OrderStatus.결제대기)
                .ios_app_scheme("")
                .android_app_scheme("")
                .partner_order_id("")
                .partner_user_id("")
                .next_redirect_app_url("")
                .next_redirect_mobile_url("")
                .next_redirect_pc_url("")
                .build();
        itemRepository.savePayReady(readyOrder);

    }

    @Test
    void findByReadyOrder(){
        Long orderId= 1231231132L;
        Optional<Order> maybeOrderReady = itemRepository.findByReadyOrder(orderId);
        if(maybeOrderReady.isEmpty()) throw  new NullPointerException("주문번호와 일치하는 결제 대기 주문이 없습니다");
        Order orderReady = maybeOrderReady.get();
        Assertions.assertThat(orderReady.getStatus()).isEqualTo(OrderStatus.결제대기);
    }

    @Test
    void findByApproveOrder(){
        Long orderId= 189457795L;
        Optional<Order> maybeOrderApproved = itemRepository.findByApproveOrder(orderId);
        if(maybeOrderApproved.isEmpty()) throw  new NullPointerException("주문번호와 일치하는 결제승인 된 주문이 없습니다");
        Order order = maybeOrderApproved.get();
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.결제승인);
    }

    @Test
    void updatePayApprove(){
        Long orderId= 1231231132L;
        Order updateParams =Order.builder()
                .approved_at(" 테스트")
                .aid(" 테스트")
                .payment_method_type(PayMethod.카카오페이)
                .status(OrderStatus.결제대기)
                .build();
        updateParams.changeStatus(OrderStatus.결제승인);
        updateParams.changePayMethod(PayMethod.카드);
        updateParams.changeApprovedAt("승인날짜");
        updateParams.changeAid("업데이트 테스트");
        Optional<Order> maybeOrder = itemRepository.updatePayApprove(orderId,updateParams);
        if(maybeOrder.isEmpty()) throw  new NullPointerException("주문번호와 일치하는 주문이 없습니다");
        Order order = maybeOrder.get();
        Assertions.assertThat(order.getAid()).isEqualTo(updateParams.getAid());
    }

    @Test
    void updatePayCancel(){
        Long orderId= 1231231132L;
        Order updateParams =Order.builder()
                .canceled_at("")
                .status(OrderStatus.결제승인)
                .build();
        updateParams.changeCancelAt("취소 테스트");
        updateParams.changeStatus(OrderStatus.결제취소);
        Optional<Order> maybeOrder = itemRepository.updatePayCancel(orderId,updateParams);
        if(maybeOrder.isEmpty()) throw  new NullPointerException("주문번호와 일치하는 주문이 없습니다");
        Order order = maybeOrder.get();
        Assertions.assertThat(order.getCanceled_at()).isEqualTo(updateParams.getCanceled_at());
    }

}