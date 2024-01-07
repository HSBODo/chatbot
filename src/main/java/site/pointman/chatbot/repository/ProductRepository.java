package site.pointman.chatbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.customrepository.ProductCustomRepository;

public interface ProductRepository extends JpaRepository<Product,Long>, ProductCustomRepository {
    @Query(value = "SELECT p FROM Product p Join fetch p.member Join fetch p.productImages WHERE p.status=:status AND p.member.userKey=:userKey AND p.isUse=:isUse",
        countQuery = "SELECT count(p) FROM Product p WHERE p.status=:status AND p.member.userKey=:userKey AND p.isUse=:isUse"
    )
    Page<Product> findByStatusAndUserKey(@Param("isUse") boolean isUse, @Param("status") ProductStatus status, @Param("userKey") String userKey, Pageable pageable);

}
