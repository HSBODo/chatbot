package site.pointman.chatbot.domain.order.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.order.service.OrderService;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.service.PaymentService;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.exception.FailOrder;
import site.pointman.chatbot.exception.NotFoundOrder;
import site.pointman.chatbot.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private boolean isUse = true;

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Override
    public void addOrder(Long orderId, String pgToken) {
        //결제 승인
        PaymentInfo approvePaymentInfo = attemptPayment(orderId, pgToken);

        try {
            //주문 정보 저장
            saveOrder(approvePaymentInfo);
        }catch (Exception e) {
            //주문저장 실패시 결제 취소 로직
            paymentService.kakaoPaymentCancel(approvePaymentInfo.getOrderId());
            throw new FailOrder("주문정보 저장을 실패하였습니다.");
        }
    }

    @Transactional
    private PaymentInfo attemptPayment(Long orderId, String pgToken){
        //결제 준비 정보 가져오기
        PaymentInfo paymentReady = paymentService.getPaymentReady(orderId);

        //결제완료
        PaymentInfo approvePaymentInfo = paymentService.kakaoPaymentApprove(pgToken, paymentReady);
        return approvePaymentInfo;
    }

    @Transactional
    private void saveOrder(PaymentInfo approvePaymentInfo){
        Member buyerMember = approvePaymentInfo.getBuyerMember();
        Product product = approvePaymentInfo.getProduct();

        Order order = Order.createOrder(
                approvePaymentInfo.getOrderId(),
                buyerMember,
                product,
                approvePaymentInfo
        );

        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findByAll();

        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByOrderStatus(status);

        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);

        return mayBeOrder;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);
        if (mayBeOrder.isEmpty()) throw new IllegalArgumentException("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();
        if (!order.isTrading()) throw new IllegalStateException("주문 체결상태 주문이 아닙니다.");

        //카카오페이 결제 취소 및 결제정보 상태변경
        paymentService.kakaoPaymentCancel(orderId);

        //주문 및 상품 상태변경

        /**
         * 주문 취소시
         * 1. 상품(product)은 판매중 상태로 변경
         * 2. 결제정보(paymentInfo)는 결제취소 상태로 변경
         */
        order.orderCancel();
    }

    @Override
    @Transactional
    public void successOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId, OrderStatus.주문체결);
        if(mayBeOrder.isEmpty()) throw new IllegalStateException("주문 체결상태 주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();
        //운송장 번호 입력했는가?
        if(order.isInputTrackingNumber()) throw new IllegalStateException("운송장번호가 입력되어있지 않습니다.");

        //구매자는 구매확정, 판매자는 판매확정을 하였는가?
        if (!order.isConfirm())throw new IllegalStateException("구매확정,판매확정 상태가 아닙니다.");

        order.orderSuccessConfirm();
    }

    @Override
    @Transactional
    public void purchaseConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) throw new NotFoundOrder("체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();

        order.buyerConfirmStatus();
    }

    @Override
    @Transactional
    public void salesConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) throw new NotFoundOrder("체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();

        order.sellerConfirmStatus();
        order.orderSuccessConfirm();
    }

    @Override
    @Transactional
    public void updateTrackingNumber(String orderId, String trackingNumber) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId),OrderStatus.주문체결);
        if (mayBeOrder.isEmpty())throw new NotFoundOrder("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();

        order.changeTrackingNumber(trackingNumber);
    }


    @Override
    @Transactional(readOnly = true)
    public Order getSalesContractProduct(String userKey, Long orderId) {
        Order order = orderRepository.findByOrderId(orderId, OrderStatus.주문체결)
                .orElseThrow(() -> new NotFoundOrder("주문체결된 주문이 존재하지 않습니다."));

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getPurchaseProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey, OrderStatus.주문취소, PageRequest.of(pageNumber, 10, sort));

        return purchaseOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getPurchaseProduct(String userKey, Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundOrder("주문체결된 주문이 존재하지 않습니다."));

        return order;
    }
}
