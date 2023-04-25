package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> findByDisplayItems();
    List<Order> findByApproveOrders(String kakaoUserkey);
    Optional<Item> findByItem(Long itemCode);
    Order savePayReady(Order order);
    Optional<Order> findByReadyOrder(Long orderId);

    Optional<Order> findByApproveOrder(Long orderId);

    Optional<Order> findByOrder(String kakaoUserkey, Long orderId);
    Optional<Order> updatePayApprove(Long orderId,Order updateParams);

    Optional<Order> updatePayCancel(Long orderId,Order updateParams);
}
