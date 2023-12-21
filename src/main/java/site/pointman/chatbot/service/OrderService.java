package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.payment.PaymentInfo;

public interface OrderService {
    Long addOrder(PaymentInfo paymentInfo);
    Long cancelOrder(Long orderId);
    Object getPurchaseProducts(String userKey);
    Object getPurchaseProductProfile(String userKey,String orderId);
}
