package site.pointman.chatbot.domain.order.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.log.response.Response;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * 구매확정
     */
    void purchaseConfirm(String orderId);

    /**
     * 판매확정
     */
    void salesConfirm(String orderId);

    /**
     * 운송장번호 입력 및 변경
     */
    void updateTrackingNumber(String orderId, String trackingNumber);

    /**
     * 주문생성
     * 카카오페이 결제 완료 -> 카카오페이 결제정보 저장 -> 주문생성 -> 주문저장 -> 상품상태 변경 -> 결제완료
     */
    void addOrder(Long orderId, String pgToken);

    /**
     * 주문생성
     * 카카오페이 결제 취소 -> 결제취소 정보 업데이트 -> 주문정보 주문취소 업데이트 -> 상품 상태 판매중 업데이트 -> 결제취소 완료
     */
    void cancelOrder(Long orderId);

    /**
     * 주문 거래 확정
     * 주문 상태 거래완료 업데이트 -> 상품 상태 판매완료 -> 거래완료
     */
    void successOrder(Long orderId);

    List<Order> getOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
    Optional<Order> getOrder(Long orderId);
    Order getSalesContractProduct(String userKey, Long orderId);
    Page<Order> getPurchaseProducts(String userKey, int pageNumber);
    Order getPurchaseProduct(String userKey, Long orderId);
}
