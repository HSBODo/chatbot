package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;

public interface OrderService {
    String kakaoPaymentReady(Product product, Member member);
}
