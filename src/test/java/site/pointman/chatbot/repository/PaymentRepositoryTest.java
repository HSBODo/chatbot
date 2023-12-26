package site.pointman.chatbot.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.constant.PaymentStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;

    private String userKey;
    private Long productId;
    private Long readyPaymentId;
    private Long approvePaymentId;


    @BeforeEach
    public void setUp() throws JsonProcessingException {
        userKey = "QFJSyeIZbO77";
        productId = 883586L;
        readyPaymentId = 938231L;
        approvePaymentId = 187911L;
    }

    @Test
    @Transactional
    void save() {
        //give
        Product product = Product.builder()
                .id(productId)
                .build();
        Member member = Member.builder()
                .userKey(userKey)
                .build();
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .orderId(123123L)
                .buyerMember(member)
                .product(product)
                .build();

        //when
        PaymentInfo savePayReady = paymentRepository.save(paymentInfo);

        //then
        Assertions.assertThat(savePayReady.getOrderId()).isEqualTo(paymentInfo.getOrderId());
    }

    @Test
    @Transactional
    void findByPaymentReadyStatus() {

        //when
        PaymentInfo paymentInfo = paymentRepository.findByPaymentStatus(readyPaymentId,PaymentStatus.결제준비).get();

        //then
        Assertions.assertThat(paymentInfo.getStatus()).isEqualTo(PaymentStatus.결제준비);
    }

    @Test
    @Transactional
    void findByPaymentSuccessStatus() {

        //when
        PaymentInfo paymentInfo = paymentRepository.findByPaymentStatus(approvePaymentId,PaymentStatus.결제완료).get();

        //then
        Assertions.assertThat(paymentInfo.getStatus()).isEqualTo(PaymentStatus.결제완료);
    }
}