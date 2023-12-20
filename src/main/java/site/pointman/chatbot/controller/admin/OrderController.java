package site.pointman.chatbot.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.service.PaymentService;

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
        String kakaoPaymentReadyUrl = paymentService.getKakaoPaymentReadyUrl(productId,userKey);
        return "redirect:"+kakaoPaymentReadyUrl;
    }

    @GetMapping(value = "kakaopay-approve/{orderId}")
    public String kakaoPayApprove (@PathVariable Long orderId, @RequestParam(value = "pg_token") String pgToken) throws Exception{
        String redirectUrl = paymentService.kakaoPaymentApprove(orderId, pgToken);
        return "redirect:"+redirectUrl;
    }

    @PostMapping(value = "kakaopay-cancel/{orderId}")
    public String kakaoPayCancel (@PathVariable Long orderId) throws Exception{
        String redirectUrl = paymentService.kakaoPaymentCancel(orderId);
        return "redirect:"+redirectUrl;
    }

    @PostMapping(value = "kakaopay-fail/{orderId}")
    public String kakaoPayFail (@PathVariable Long orderId) throws Exception{
        String redirectUrl = paymentService.kakaoPaymentCancel(orderId);
        return "redirect:"+redirectUrl;
    }

}
