package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.annotation.SkipLogging;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.PaymentService;
import site.pointman.chatbot.view.kakaochatobotview.OrderChatBotView;

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
    OrderChatBotView orderChatBotView;

    public OrderController(PaymentService paymentService, OrderService orderService, OrderChatBotView orderChatBotView) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.orderChatBotView = orderChatBotView;
    }

    @SkipLogging
    @GetMapping(value = "kakaopay-ready/{productId}")
    public String kakaoPayReady (@PathVariable Long productId, @RequestParam String userKey) throws Exception {
        try {
            KakaoPaymentReadyResponse kakaoPaymentReadyResponse = paymentService.kakaoPaymentReady(productId, userKey);
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
            Response result = orderService.addOrder(orderId, pgToken);
            if (result.getCode() != ResultCode.OK.getValue()) return "redirect:"+KAKAO_CHANNEL_URL+"/"+URLEncoder.encode("결제실패", "UTF-8");

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
    public ChatBotResponse updateTrackingNumber (@RequestBody ChatBotRequest chatBotRequest) {
        String trackingNumber = chatBotRequest.getTrackingNumber();
        String orderId = chatBotRequest.getOrderId();
        return orderChatBotView.updateTrackingNumber(orderId,trackingNumber);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/purchaseSuccessReconfirm")
    public ChatBotResponse purchaseSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.purchaseReconfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/purchaseSuccessConfirm")
    public ChatBotResponse purchaseSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.purchaseConfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/saleSuccessReconfirm")
    public ChatBotResponse saleSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.salesReconfirm(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/saleSuccessConfirm")
    public ChatBotResponse saleSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.salesConfirm(orderId);
    }

}
