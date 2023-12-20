package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long addOrder(PaymentInfo paymentInfo) {

        Order order = Order.builder()
                .orderId(paymentInfo.getOrderId())
                .buyerMember(paymentInfo.getBuyerMember())
                .product(paymentInfo.getProduct())
                .quantity(paymentInfo.getQuantity())
                .status(OrderStatus.결제완료)
                .build();

        Long orderId = orderRepository.save(order);
        return orderId;
    }
}
