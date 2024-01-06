package site.pointman.chatbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.customrepository.OrderCustomRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderCustomRepository {

    @Query("SELECT o FROM Order o WHERE o.buyerMember.userKey =:userKey AND o.status <>:status AND o.isUse=true")
    Page<Order> findByBuyerUserKey(@Param("userKey") String userKey, @Param("status") OrderStatus status, Pageable pageable);

}
