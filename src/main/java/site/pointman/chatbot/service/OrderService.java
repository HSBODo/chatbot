package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.payment.PaymentInfo;
import site.pointman.chatbot.domain.response.HttpResponse;

public interface OrderService {
    Long addOrder(PaymentInfo paymentInfo);
    Long cancelOrder(Long orderId);
    HttpResponse successOrder(Long orderId);
    Object updateTrackingNumber(String orderId, String trackingNumber);
    Object getPurchaseProducts(String userKey);
    Object getPurchaseProductProfile(String userKey,String orderId);
}
