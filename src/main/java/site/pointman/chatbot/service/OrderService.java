package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.product.Product;

public interface OrderService {
    Long addOrder(PaymentInfo paymentInfo);
}
