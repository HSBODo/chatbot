package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.annotation.SkipLogging;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping(value = "order")
public class OrderController {

    @Value("${kakao.channel.url}")
    private String KAKAO_CHANNEL_URL;

    PaymentService paymentService;
    OrderService orderService;

    public OrderController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @SkipLogging
    @GetMapping(value = "kakaopay-ready/{productId}")
    public String kakaoPayReady (@PathVariable Long productId, @RequestParam String userKey) throws Exception {
        try {
            KakaoPaymentReadyResponse kakaoPaymentReadyResponse = paymentService.getKakaoPaymentReadyUrl(productId, userKey);
            String next_redirect_app_url = kakaoPaymentReadyResponse.getNext_redirect_app_url();

            return "redirect:"+next_redirect_app_url;
        }catch (Exception e) {
            return "redirect:"+KAKAO_CHANNEL_URL+"/"+URLEncoder.encode("결제실패", "UTF-8");
        }
    }

    @SkipLogging
    @GetMapping(value = "kakaopay-approve/{orderId}")
    public String kakaoPayApprove (@PathVariable Long orderId, @RequestParam(value = "pg_token") String pgToken) throws UnsupportedEncodingException {
        try {
            paymentService.kakaoPaymentApprove(orderId, pgToken);

            return "redirect:"+KAKAO_CHANNEL_URL+"/"+ URLEncoder.encode("결제성공", "UTF-8");
        }catch (Exception e) {
            return "redirect:"+KAKAO_CHANNEL_URL+"/"+URLEncoder.encode("결제실패", "UTF-8");
        }
    }

    @SkipLogging
    @PostMapping(value = "kakaopay-fail/{orderId}")
    public String kakaoPayFail (@RequestParam String request) {
        log.info("request={}",request);
        return null;
    }

    @ResponseBody
    @PostMapping(value = "PATCH/trackingNumber")
    public Object updateTrackingNumber (@RequestBody ChatBotRequest chatBotRequest) {
        String trackingNumber = chatBotRequest.getTrackingNumber();
        String orderId = chatBotRequest.getOrderId();
        return orderService.updateTrackingNumber(orderId,trackingNumber);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/purchaseSuccessReconfirm")
    public Object purchaseSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderService.purchaseSuccessReconfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/purchaseSuccessConfirm")
    public Object purchaseSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderService.purchaseSuccessConfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/saleSuccessReconfirm")
    public Object saleSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderService.saleSuccessReconfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/saleSuccessConfirm")
    public Object saleSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderService.saleSuccessConfirm(orderId);
    }

}
