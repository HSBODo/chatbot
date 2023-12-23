package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.OrderMemberConfirmStatus;
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
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;
import site.pointman.chatbot.service.chatbot.OrderChatBotResponseService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    PaymentRepository paymentRepository;

    PaymentService paymentService;


    OrderChatBotResponseService orderChatBotResponseService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, PaymentService paymentService, PaymentRepository paymentRepository, OrderChatBotResponseService orderChatBotResponseService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.orderChatBotResponseService = orderChatBotResponseService;
    }

    @Override
    public Long addOrder(Long orderId, String pgToken) {
        Optional<PaymentInfo> maybePaymentInfo = paymentRepository.findByPaymentReadyStatus(orderId);

        if(maybePaymentInfo.isEmpty()) throw new IllegalArgumentException("결제준비중인 주문이 존재하지 않습니다.");

        PaymentInfo paymentReadyInfo = maybePaymentInfo.get();

        paymentService.kakaoPaymentApprove(pgToken,paymentReadyInfo);

        try {
            Member buyerMember = paymentReadyInfo.getBuyerMember();
            Product product = paymentReadyInfo.getProduct();


            Order order = Order.builder()
                    .orderId(paymentReadyInfo.getOrderId())
                    .buyerMember(buyerMember)
                    .product(product)
                    .quantity(paymentReadyInfo.getQuantity())
                    .status(OrderStatus.주문체결)
                    .paymentInfo(paymentReadyInfo)
                    .build();

            addOrderTransactional(order,product);
        }catch (Exception e) {
            paymentService.kakaoPaymentCancel(paymentReadyInfo);
            throw new RuntimeException("결제 실패");
        }
        return paymentReadyInfo.getOrderId();
    }

    @Transactional
    private void addOrderTransactional(Order order, Product product){
        orderRepository.save(order);
        productRepository.updateStatus(product.getId(), ProductStatus.판매대기);
    }

    @Override
    @Transactional
    public Long cancelOrder(Long orderId) {
        Optional<PaymentInfo> maybeSuccessPaymentInfo = paymentRepository.findByPaymentSuccessStatus(orderId);

        if(maybeSuccessPaymentInfo.isEmpty()) throw new IllegalArgumentException("결제승인 주문이 존재하지 않습니다.");
        PaymentInfo successPaymentInfo = maybeSuccessPaymentInfo.get();

        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);
        if (mayBeOrder.isEmpty()) throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        OrderStatus status = order.getStatus();
        if(!status.equals(OrderStatus.주문체결)) throw new IllegalArgumentException("주문체결된 주문이 아닙니다.");

        //카카오페이 결제 취소 및 결제정보 상태변경
        paymentService.kakaoPaymentCancel(successPaymentInfo);

        //주문 및 상품 상태변경
        Long productId = order.getProduct().getId();
        /**
         * 주문 취소시
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

        List<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey);
        if (purchaseOrders.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

        return orderChatBotResponseService.getPurchaseProducts(purchaseOrders);
    }

    @Override
    public Object getPurchaseProductProfile(String userKey, String orderId) {
        try {
            Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId));
            if (mayBeOrder.isEmpty()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");

            Order order = mayBeOrder.get();

            return orderChatBotResponseService.getPurchaseProductProfile(order);
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
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
        order.changeBuyerConfirmStatus(OrderMemberConfirmStatus.구매확정);

        return orderChatBotResponseService.purchaseSuccessConfirm(order);
    }

    @Override
    public ChatBotResponse saleSuccessReconfirm(String orderId) {
        return orderChatBotResponseService.saleSuccessReconfirm(orderId);
    }

    @Override
    @Transactional
    public ChatBotResponse saleSuccessConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new ChatBotExceptionResponse().createException("체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        order.changeSellerConfirmStatus(OrderMemberConfirmStatus.판매확정);
        order.orderSuccessConfirm();
        return orderChatBotResponseService.saleSuccessConfirm(order);
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
