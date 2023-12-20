package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentCancelResponse;
import site.pointman.chatbot.domain.payment.kakaopay.KakaoPaymentReadyResponse;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SpringBootTest
@Slf4j
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    private Long productId;
    private String userKey;

    @BeforeEach
    public void setUp(){
        productId = 648502L;
        userKey = "QFJSyeIZbO77" ;
    }

    @Test
    @Transactional
    void kakaoPaymentReady() throws UnsupportedEncodingException {

        //when
        KakaoPaymentReadyResponse kakaoPaymentReadyResponse = paymentService.getKakaoPaymentReadyUrl(productId, userKey);

        //then
        Assertions.assertThat(kakaoPaymentReadyResponse.getTid()).isNotNull();
    }

    @Test
    @Transactional
    void kakaoPaymentApprove() throws Exception {
        Long orderId = 595936L;
        String pgToken = "asdasd";

        //when
        //String kakaoPaymentReadyUrl = paymentService.kakaoPaymentApprove(orderId,pgToken);

        //then
        //Assertions.assertThat(kakaoPaymentReadyUrl).doesNotContain("결제실패");
    }

    @Test
    void kakaoPaymentCancel() throws Exception {
        //given
        Long approveOrderId = 604891L;

        //when
        KakaoPaymentCancelResponse kakaoPaymentCancelResponse = paymentService.kakaoPaymentCancel(approveOrderId);

        //then
        Assertions.assertThat(kakaoPaymentCancelResponse.getTid()).isNotNull();
    }
}