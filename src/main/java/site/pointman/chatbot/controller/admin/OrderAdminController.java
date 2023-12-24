package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping(value = "admin/order")
public class OrderAdminController {

    @Value("${kakao.channel.url}")
    private String KAKAO_CHANNEL_URL;


    OrderService orderService;

    public OrderAdminController(OrderService orderService) {

        this.orderService = orderService;
    }

    @ResponseBody
    @PostMapping(value = "kakaopay-cancel/{orderId}")
    public HttpResponse kakaoPayCancel (@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);

            return new HttpResponse(ApiResultCode.OK,"주문번호 "+orderId+"의 주문을 정상적으로 취소하였습니다.");
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"주문번호 "+orderId+"의 주문취소를 실패하였습니다.");
        }
    }

    @ResponseBody
    @PatchMapping(value = "/success/{orderId}")
    public HttpResponse orderSuccess (@PathVariable Long orderId) {
        return orderService.successOrder(orderId);
    }

    @ResponseBody
    @GetMapping(value = "")
    public Object getOrders () {
        return orderService.getOrders();
    }

    @ResponseBody
    @GetMapping(value = "status")
    public Object getOrdersByStatus (@RequestParam("status") OrderStatus orderStatus) {
        return orderService.getOrders(orderStatus);
    }

}
