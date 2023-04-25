package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface KaKaoItemRepository {
    List<Item> findByDisplayItems();
    List<Order> findByOrders(String kakaoUserkey);
    Optional<Item> findByItem(Long itemCode);
    Order savePayReady(Order order);
    Optional<Order> findByReadyOrder(Long orderId);

    Optional<Order> findByApproveOrder(Long orderId);

    Optional<Order> findByOrder(String kakaoUserkey, Long orderId);
    void updatePayApprove(Order order);

    void updatePayCancel(Order order);
}
