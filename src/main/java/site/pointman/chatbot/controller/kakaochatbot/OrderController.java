package site.pointman.chatbot.controller.kakaochatbot;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.exception.NotFoundOrder;
import site.pointman.chatbot.handler.annotation.SkipLogging;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.order.service.OrderService;
import site.pointman.chatbot.domain.payment.service.PaymentService;
import site.pointman.chatbot.view.kakaochatobotview.OrderChatBotView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequestMapping(value = "order")
@RequiredArgsConstructor
public class OrderController {

    @Value("${kakao.channel.url}")
    private String KAKAO_CHANNEL_URL;

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OrderChatBotView orderChatBotView;
    private final ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

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
            orderService.addOrder(orderId, pgToken);

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
        try {
            String trackingNumber = chatBotRequest.getTrackingNumber();
            String orderId = chatBotRequest.getOrderId();

            orderService.updateTrackingNumber(orderId,trackingNumber);

            return orderChatBotView.updateTrackingNumberResultPage();
        }catch (NotFoundOrder e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }

    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/purchaseSuccessReconfirm")
    public ChatBotResponse purchaseSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.purchaseReconfirmPage(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/purchaseSuccessConfirm")
    public ChatBotResponse purchaseSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String orderId = chatBotRequest.getOrderId();

            orderService.purchaseConfirm(orderId);

            return orderChatBotView.purchaseConfirmResultPage();
        }catch (NotFoundOrder e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }

    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/GET/saleSuccessReconfirm")
    public ChatBotResponse saleSuccessReconfirm (@RequestBody ChatBotRequest chatBotRequest) {
        String orderId = chatBotRequest.getOrderId();

        return orderChatBotView.salesReconfirmPage(orderId);
    }

    @ResponseBody
    @PostMapping(value = "kakaochatbot/POST/saleSuccessConfirm")
    public ChatBotResponse saleSuccessConfirmation (@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String orderId = chatBotRequest.getOrderId();
            orderService.salesConfirm(orderId);

            return orderChatBotView.salesConfirmResultPage();
        }catch (NotFoundOrder e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

}
