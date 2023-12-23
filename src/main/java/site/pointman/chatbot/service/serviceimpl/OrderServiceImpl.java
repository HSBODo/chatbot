package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.chatbot.OrderChatBotResponseService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderChatBotResponseService orderChatBotResponseService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderChatBotResponseService orderChatBotResponseService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderChatBotResponseService = orderChatBotResponseService;
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
                .status(OrderStatus.주문체결)
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

        if(!status.equals(OrderStatus.주문체결)) throw new IllegalArgumentException("주문체결된 주문이 아닙니다.");

        Long productId = order.getProduct().getId();

        /**
         * 결제 취소시
         * 1. 상품(product)은 판매중 상태로 변경
         * 2. 결제정보(paymentInfo)는 결제취소 상태로 변경
         */
        productRepository.updateStatus(productId, ProductStatus.판매중);
        order.changeStatus(OrderStatus.주문취소);

        return order.getOrderId();
    }

    @Override
    @Transactional
    public HttpResponse successOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId, OrderStatus.주문체결);
        if(mayBeOrder.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        Member buyerMember = order.getBuyerMember();
        Product product = order.getProduct();
        String trackingNumber = order.getTrackingNumber();
        if(trackingNumber.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"운송장번호가 입력되어있지 않습니다.");

        product.changeBuyerMemberUserKey(buyerMember.getUserKey());
        product.changeStatus(ProductStatus.판매완료);
        order.changeStatus(OrderStatus.거래완료);

        return new HttpResponse(ApiResultCode.OK,"주문이 정상적으로 거래가 완료되었습니다. 주문번호= "+order.getOrderId());
    }

    @Override
    public Object getPurchaseProducts(String userKey) {

        List<Order> purchaseOrders = orderRepository.findByUserKey(userKey);
        if (purchaseOrders.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        return orderChatBotResponseService.getPurchaseProducts(purchaseOrders);
    }

    @Override
    public Object getPurchaseProductProfile(String userKey, String orderId) {

        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId));
        if (mayBeOrder.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        Order order = mayBeOrder.get();

        return orderChatBotResponseService.getPurchaseProductProfile(order);
    }

    @Override
    public ChatBotResponse purchaseSuccessReconfirm(String orderId) {

        return orderChatBotResponseService.purchaseSuccessReconfirm(orderId);
    }

    @Override
    @Transactional
    public ChatBotResponse purchaseSuccessConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new ChatBotExceptionResponse().createException("체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        order.changeStatus(OrderStatus.구매자확인);

        return orderChatBotResponseService.purchaseSuccessConfirm(order);
    }

    @Override
    @Transactional
    public Object updateTrackingNumber(String orderId, String trackingNumber) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId),OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new ChatBotExceptionResponse().createException("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();

        order.changeTrackingNumber(trackingNumber);

        return orderChatBotResponseService.updateTrackingNumber();
    }
}
