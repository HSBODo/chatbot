package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.repository.PaymentRepository;
import site.pointman.chatbot.domain.order.service.OrderService;

import java.nio.charset.Charset;
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
    public ResponseEntity getOrders () {
        HttpHeaders headers = getHeaders();

        List<Order> orders = orderService.getOrders();
        if (orders.isEmpty()) return new ResponseEntity(new Response(ResultCode.EXCEPTION,"주문이 존재하지 않습니다."),headers, HttpStatus.OK);

        return new ResponseEntity(orders,headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "")
    public ResponseEntity getOrdersByStatus (@RequestParam("status") OrderStatus orderStatus) {
        HttpHeaders headers = getHeaders();

        List<Order> orders = orderService.getOrdersByStatus(orderStatus);
        if (orders.isEmpty()) return new ResponseEntity(new Response(ResultCode.EXCEPTION,"주문이 존재하지 않습니다."),headers, HttpStatus.OK);

        return new ResponseEntity(orders,headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "{orderId}")
    public ResponseEntity getOrderByOrderId (@PathVariable("orderId") Long orderId) {
        HttpHeaders headers = getHeaders();

        Optional<Order> maybeOrder = orderService.getOrder(orderId);
        if (maybeOrder.isEmpty()) return new ResponseEntity(new Response(ResultCode.EXCEPTION,"주문이 존재하지 않습니다."),headers, HttpStatus.OK);
        Order order = maybeOrder.get();

        return new ResponseEntity(order,headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "paymentInfo/{orderId}")
    public ResponseEntity getPaymentInfoByOrderId (@PathVariable("orderId") Long orderId) {
        HttpHeaders headers = getHeaders();

        Optional<PaymentInfo> mayBePaymentInfo = paymentRepository.findByOrderId(orderId);
        if (mayBePaymentInfo.isEmpty()) return new ResponseEntity(new Response(ResultCode.EXCEPTION,"결제 정보가 없습니다."),headers, HttpStatus.OK);
        PaymentInfo paymentInfo = mayBePaymentInfo.get();

        return new ResponseEntity(paymentInfo,headers, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/success/{orderId}")
    public ResponseEntity orderSuccess (@PathVariable Long orderId) {
        HttpHeaders headers = getHeaders();
        try {
            orderService.successOrder(orderId);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 주문 거래 확정 하였습니다.",orderId),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(new Response<>(ResultCode.EXCEPTION,"주문 거래 확정을 실패하였습니다",e.getMessage()),headers, HttpStatus.OK);
        }
    }

    @ResponseBody
    @PostMapping(value = "kakaopay-cancel/{orderId}")
    public ResponseEntity kakaoPayCancel (@PathVariable Long orderId) {
        HttpHeaders headers = getHeaders();
        try {
            orderService.cancelOrder(orderId);

            return new ResponseEntity(new Response<>(ResultCode.OK,"성공적으로 주문을 취소하였습니다.",orderId),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity(new Response(ResultCode.FAIL,"주문번호 "+orderId+"의 주문취소를 실패하였습니다.",e.getMessage()),headers, HttpStatus.OK);
        }
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
