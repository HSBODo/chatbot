package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.constant.order.OrderStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    private String userKey;
    private Long productId;

    @BeforeEach
    public void setUp(){
        userKey = "QFJSyeIZbO77";
        productId = 883586L;
    }

    @Test
    @Transactional
    void save() {
        //give
        Member member = Member.builder()
                .userKey(userKey)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .orderId(213213L)
                .build();

        Order order = Order.builder()
                .orderId(123123L)
                .product(product)
                .buyerMember(member)
                .paymentInfo(paymentInfo)
                .quantity(1)
                .status(OrderStatus.주문체결)
                .build();

        //when
        Order saveOrder = orderRepository.save(order);

        //then
        Assertions.assertThat(saveOrder.getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    @Transactional
    void findByOrderId() {

        //give
        Long orderId = 512054L;

        //when
        Order order = orderRepository.findByOrderId(orderId).get();

        //then
        Assertions.assertThat(order.getOrderId()).isEqualTo(orderId);
    }

    @Test
    @Transactional
    void findByUserKey() {
        String buyerUserKey = userKey;

        //when
        List<Order> buyOrders = orderRepository.findByBuyerUserKey(userKey);

        //then
        buyOrders.forEach(order -> {
            Assertions.assertThat(order.getBuyerMember().getUserKey()).isEqualTo(buyerUserKey);
        });

    }
}