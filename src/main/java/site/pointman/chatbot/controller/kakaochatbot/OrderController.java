package site.pointman.chatbot.controller.kakaochatbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
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

    public OrderController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

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

    @GetMapping(value = "kakaopay-approve/{orderId}")
    public String kakaoPayApprove (@PathVariable Long orderId, @RequestParam(value = "pg_token") String pgToken) throws UnsupportedEncodingException {
        try {
            paymentService.kakaoPaymentApprove(orderId, pgToken);

            return "redirect:"+KAKAO_CHANNEL_URL+"/"+ URLEncoder.encode("결제성공", "UTF-8");
        }catch (Exception e) {
            return "redirect:"+KAKAO_CHANNEL_URL+"/"+URLEncoder.encode("결제실패", "UTF-8");
        }
    }

    @PostMapping(value = "kakaopay-fail/{orderId}")
    public String kakaoPayFail (@RequestParam String request) {
        log.info("request={}",request);
        return null;
    }

}