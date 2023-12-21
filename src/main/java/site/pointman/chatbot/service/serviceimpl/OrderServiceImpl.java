package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.utill.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

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
                .paymentInfo(paymentInfo)
                .build();

        Long orderId = orderRepository.save(order);

        productRepository.updateStatus(product.getId(), ProductStatus.판매대기);

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

    @Override
    public Object getPurchaseProducts(String userKey) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Button button = new Button(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();

        List<Order> purchaseOrders = orderRepository.findByUserKey(userKey);
        if (purchaseOrders.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        purchaseOrders.forEach(order -> {
            BasicCard basicCard = new BasicCard();

            Product product = order.getProduct();
            String productId = String.valueOf(product.getId());
            String status = product.getStatus().getOppositeValue();

            String title = product.getName();
            String description = new StringBuilder()
                    .append("판매가격: "+ StringUtils.formatPrice(product.getPrice())+"원")
                    .append("\n")
                    .append("상품상태: "+status)
                    .toString();

            Button detailButton =new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE_PROFILE.getBlockId(), ButtonParamKey.productId, productId);
            basicCard.setThumbnail(product.getProductImages().getImageUrls().get(0),true);
            basicCard.setTitle(title);
            basicCard.setDescription(description);
            basicCard.setButton(detailButton);

            basicCardCarousel.addComponent(basicCard);
        });

        chatBotResponse.addCarousel(basicCardCarousel);
        chatBotResponse.addQuickButton(button);
        return chatBotResponse;
    }
}
