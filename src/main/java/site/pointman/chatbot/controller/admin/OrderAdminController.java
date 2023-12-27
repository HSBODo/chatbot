package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public HttpResponse getOrders () {
        return orderService.getOrders();
    }

    @ResponseBody
    @GetMapping(value = "")
    public HttpResponse getOrdersByStatus (@RequestParam("status") OrderStatus orderStatus) {
        return orderService.getOrders(orderStatus);
    }

    @ResponseBody
    @GetMapping(value = "{orderId}")
    public HttpResponse getOrderByOrderId (@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }

    @ResponseBody
    @GetMapping(value = "paymentInfo/{orderId}")
    public Object getPaymentInfoByOrderId (@PathVariable("orderId") Long orderId) {
        Optional<PaymentInfo> mayBePaymentInfo = paymentRepository.findByOrderId(orderId);
        if (mayBePaymentInfo.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"결제정보가 없습니다.");
        PaymentInfo paymentInfo = mayBePaymentInfo.get();

        return paymentInfo;
    }

    @ResponseBody
    @PostMapping(value = "/success/{orderId}")
    public HttpResponse orderSuccess (@PathVariable Long orderId) {
        return orderService.successOrder(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaopay-cancel/{orderId}")
    public HttpResponse kakaoPayCancel (@PathVariable Long orderId) {
        try {
            return  orderService.cancelOrder(orderId);
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"주문번호 "+orderId+"의 주문취소를 실패하였습니다.");
        }
    }
}
