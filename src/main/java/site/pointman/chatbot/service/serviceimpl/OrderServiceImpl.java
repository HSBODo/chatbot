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
import site.pointman.chatbot.domain.response.property.components.TextCard;
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
                .trackingNumber("미입력")
                .status(OrderStatus.결제체결)
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

        if(!status.equals(OrderStatus.결제체결)) throw new IllegalArgumentException("결제체결된 주문이 아닙니다.");

        Long productId = order.getProduct().getId();

        productRepository.updateStatus(productId, ProductStatus.판매중);
        order.changeStatus(OrderStatus.결제취소);

        return order.getOrderId();
    }

    @Override
    public Object getPurchaseProducts(String userKey) {

        List<Order> purchaseOrders = orderRepository.findByUserKey(userKey);
        if (purchaseOrders.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Button button = new Button(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();

        purchaseOrders.forEach(order -> {
            BasicCard basicCard = new BasicCard();

            Product product = order.getProduct();

            String orderId = String.valueOf(order.getOrderId());
            String status = product.getStatus().getOppositeValue();

            String title = product.getName();
            String description = new StringBuilder()
                    .append("판매가격: "+ StringUtils.formatPrice(product.getPrice())+"원")
                    .append("\n")
                    .append("상품상태: "+status)
                    .toString();

            Button detailButton =new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE_PROFILE.getBlockId(), ButtonParamKey.orderId, orderId);
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

    @Override
    public Object getPurchaseProductProfile(String userKey, String orderId) {

        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId));
        if (mayBeOrder.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        Order order = mayBeOrder.get();
        Product product = order.getProduct();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);

        textCard.setTitle(product.getName());
        textCard.setDescription(order.getPurchaseProductProfile());

        chatBotResponse.addTextCard(textCard);
        if(!order.getTrackingNumber().equals("미입력")) chatBotResponse.addQuickButton(new Button(ButtonName.구매확정.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId()));
        chatBotResponse.addQuickButton(new Button(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId()));
        return chatBotResponse;
    }

    @Override
    @Transactional
    public Object updateTrackingNumber(String productId, String trackingNumber) {
        Optional<Order> mayBeOrder = orderRepository.findByProductId(Long.parseLong(productId),OrderStatus.결제체결);
        if (mayBeOrder.isEmpty()) return new ChatBotExceptionResponse().createException("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();

        order.changeTrackingNumber(trackingNumber);

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        chatBotResponse.addSimpleText("운송장번호를 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private Carousel<BasicCard> createCarouselImage(List<String> imageUrls){
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        imageUrls.forEach(imageUrl -> {
            BasicCard basicCard = new BasicCard();
            basicCard.setThumbnail(imageUrl,true);
            basicCardCarousel.addComponent(basicCard);
        });
        return basicCardCarousel;
    }
}
