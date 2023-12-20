package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Long save(Order order);
}
