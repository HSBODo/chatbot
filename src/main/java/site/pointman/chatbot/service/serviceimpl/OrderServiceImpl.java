package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.OrderService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Long addOrder(PaymentInfo paymentInfo) {
        Member buyerMember = paymentInfo.getBuyerMember();
        Product product = paymentInfo.getProduct();

        Order order = Order.builder()
                .orderId(paymentInfo.getOrderId())
                .buyerMember(buyerMember)
                .product(product)
                .quantity(paymentInfo.getQuantity())
                .status(OrderStatus.결제완료)
                .build();

        Long orderId = orderRepository.save(order);

        productRepository.updateStatus(product.getId(), ProductStatus.결제완료_대기);

        return orderId;
    }

    @Override
    @Transactional
    public Long cancelOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);

        if (mayBeOrder.isEmpty()) throw new IllegalArgumentException("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();
        OrderStatus status = order.getStatus();

        if(!status.equals(OrderStatus.결제완료)) throw new IllegalArgumentException("결제완료된 주문이 아닙니다.");

        Long productId = order.getProduct().getId();

        productRepository.updateStatus(productId, ProductStatus.판매중);
        order.changeStatus(OrderStatus.결제취소);

        return order.getOrderId();
    }
}
