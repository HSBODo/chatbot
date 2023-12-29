package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.service.OrderService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "admin/order")
public class OrderAdminController {

    @Value("${kakao.channel.url}")
    private String KAKAO_CHANNEL_URL;


    OrderService orderService;
    PaymentRepository paymentRepository;

    public OrderAdminController(OrderService orderService, PaymentRepository paymentRepository) {
        this.orderService = orderService;
        this.paymentRepository = paymentRepository;
    }

    @ResponseBody
    @GetMapping(value = "all")
    public Object getOrders () {
        List<Order> orders = orderService.getOrders();
        if (orders.isEmpty()) return new Response(ResultCode.FAIL,"주문이 존재하지 않습니다.");

        return orders;
    }

    @ResponseBody
    @GetMapping(value = "")
    public Object getOrdersByStatus (@RequestParam("status") OrderStatus orderStatus) {
        List<Order> orders = orderService.getOrdersByStatus(orderStatus);
        if (orders.isEmpty()) return new Response(ResultCode.FAIL,"주문이 존재하지 않습니다.");
        return orders;
    }

    @ResponseBody
    @GetMapping(value = "{orderId}")
    public Object getOrderByOrderId (@PathVariable("orderId") Long orderId) {
        Optional<Order> order = orderService.getOrder(orderId);
        if (order.isEmpty()) return new Response(ResultCode.FAIL,"주문이 존재하지 않습니다.");
        return order.get();
    }

    @ResponseBody
    @GetMapping(value = "paymentInfo/{orderId}")
    public Object getPaymentInfoByOrderId (@PathVariable("orderId") Long orderId) {
        Optional<PaymentInfo> mayBePaymentInfo = paymentRepository.findByOrderId(orderId);
        if (mayBePaymentInfo.isEmpty()) return new Response(ResultCode.EXCEPTION,"결제정보가 없습니다.");
        PaymentInfo paymentInfo = mayBePaymentInfo.get();

        return paymentInfo;
    }

    @ResponseBody
    @PostMapping(value = "/success/{orderId}")
    public Response orderSuccess (@PathVariable Long orderId) {
        return orderService.successOrder(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaopay-cancel/{orderId}")
    public Response kakaoPayCancel (@PathVariable Long orderId) {
        try {
            return  orderService.cancelOrder(orderId);
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"주문번호 "+orderId+"의 주문취소를 실패하였습니다.");
        }
    }
}
