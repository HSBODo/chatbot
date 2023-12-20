package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        String kakaoPaymentReadyUrl = paymentService.getKakaoPaymentReadyUrl(productId, userKey);

        //then
        Assertions.assertThat(kakaoPaymentReadyUrl).doesNotContain("결제실패");
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
        Long approveOrderId = 548577L;

        //when
        String kakaoPaymentReadyUrl = paymentService.kakaoPaymentCancel(approveOrderId);

        //then
        String 결제취소_성공 = URLEncoder.encode("결제취소 성공");
        Assertions.assertThat(kakaoPaymentReadyUrl).contains(결제취소_성공);
    }
}