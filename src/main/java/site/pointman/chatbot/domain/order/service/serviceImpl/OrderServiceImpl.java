package site.pointman.chatbot.domain.order.service.serviceImpl;

import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.order.constatnt.OrderMemberConfirmStatus;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.exception.NotFoundOrder;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.domain.order.service.OrderService;
import site.pointman.chatbot.domain.payment.service.PaymentService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private boolean isUse = true;

    OrderRepository orderRepository;
    ProductRepository productRepository;

    PaymentService paymentService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @Override
    public Response addOrder(Long orderId, String pgToken) {
        Response result = paymentService.getPaymentInfoByStatus(orderId, PaymentStatus.결제준비);
        if (result.getCode() != ResultCode.OK.getValue()) return new Response(ResultCode.EXCEPTION, result.getMessage());
        PaymentInfo paymentReadyInfo = (PaymentInfo) result.getData();

        paymentService.kakaoPaymentApprove(pgToken,paymentReadyInfo);
        Order order;
        try {
            Member buyerMember = paymentReadyInfo.getBuyerMember();
            Product product = paymentReadyInfo.getProduct();


            order = Order.builder()
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
            return new Response(ResultCode.FAIL,"결제실패");
        }
        return new Response(ResultCode.OK,"주문성공",order);
    }

    @Transactional
    private void addOrderTransactional(Order order, Product product){
        orderRepository.save(order);
        product.changeStatus(ProductStatus.판매대기);
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findByAll();

        return orders;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByOrderStatus(status);

        return orders;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);

        return mayBeOrder;
    }

    @Override
    @Transactional
    public Response cancelOrder(Long orderId) {
        Response result = paymentService.getPaymentInfoByStatus(orderId, PaymentStatus.결제완료);
        if (result.getCode() != ResultCode.OK.getValue()) return result;

        PaymentInfo successPaymentInfo = (PaymentInfo) result.getData();

        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId);

        if (mayBeOrder.isEmpty()) throw new IllegalArgumentException("주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();
        Product product = order.getProduct();
        OrderStatus status = order.getStatus();
        if(!status.equals(OrderStatus.주문체결)) return new Response(ResultCode.EXCEPTION,"주문체결된 주문이 아닙니다.");

        //카카오페이 결제 취소 및 결제정보 상태변경
        paymentService.kakaoPaymentCancel(successPaymentInfo);

        //주문 및 상품 상태변경
        Long productId = order.getProduct().getId();
        /**
         * 주문 취소시
         * 1. 상품(product)은 판매중 상태로 변경
         * 2. 결제정보(paymentInfo)는 결제취소 상태로 변경
         */

        product.changeStatus(ProductStatus.판매중);
        order.changeStatus(OrderStatus.주문취소);
        return new Response(ResultCode.OK,"주문번호 "+orderId+"의 주문을 정상적으로 취소하였습니다.",order);
    }

    @Override
    @Transactional
    public Response successOrder(Long orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(orderId, OrderStatus.주문체결);
        if(mayBeOrder.isEmpty()) return new Response(ResultCode.FAIL,"체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        Member buyerMember = order.getBuyerMember();
        Product product = order.getProduct();

        if(StringUtils.isNullOrEmpty(order.getTrackingNumber())) return new Response(ResultCode.FAIL,"운송장번호가 입력되어있지 않습니다.");

        product.changeBuyerMemberUserKey(buyerMember.getUserKey());
        product.changeStatus(ProductStatus.판매완료);
        order.changeStatus(OrderStatus.거래완료);

        return new Response(ResultCode.OK,"주문이 정상적으로 거래가 완료되었습니다. 주문번호= "+order.getOrderId());
    }

    @Override
    @Transactional
    public Response purchaseConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new Response(ResultCode.EXCEPTION,"체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        order.changeBuyerConfirmStatus(OrderMemberConfirmStatus.구매확정);

        return new Response(ResultCode.OK,"정상적으로 구매확정 하였습니다.",order);
    }

    @Override
    @Transactional
    public Response salesConfirm(String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId), OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new Response(ResultCode.EXCEPTION,"체결된 주문이 존재하지 않습니다.");
        Order order = mayBeOrder.get();
        order.changeSellerConfirmStatus(OrderMemberConfirmStatus.판매확정);
        order.orderSuccessConfirm();
        return new Response(ResultCode.OK,"정상적으로 판매확정 하였습니다.",order);
    }

    @Override
    @Transactional
    public Response updateTrackingNumber(String orderId, String trackingNumber) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId),OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return new Response(ResultCode.EXCEPTION,"주문이 존재하지 않습니다.");

        Order order = mayBeOrder.get();

        order.changeTrackingNumber(trackingNumber);

        return new Response(ResultCode.OK,"정상적으로 운송장번호를 변경 하였습니다.",trackingNumber);
    }


    @Override
    public Order getSalesContractProduct(String userKey, Long orderId) {
        Order order = orderRepository.findByOrderId(orderId, OrderStatus.주문체결)
                .orElseThrow(() -> new NotFoundOrder("주문체결된 주문이 존재하지 않습니다."));

        return order;
    }

    @Override
    public Page<Order> getPurchaseProducts(String userKey, int pageNumber) {
        Sort sort = Sort.by("createDate").descending();
        Page<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey, OrderStatus.주문취소, PageRequest.of(pageNumber, 10, sort));

        return purchaseOrders;
    }

    @Override
    public Order getPurchaseProduct(String userKey, Long orderId) {
        Order order = orderRepository.findByOrderId(orderId,OrderStatus.주문체결)
                .orElseThrow(() -> new NotFoundOrder("주문체결된 주문이 존재하지 않습니다."));

        return order;
    }
}
