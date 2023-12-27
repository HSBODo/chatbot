package site.pointman.chatbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.customrepository.ProductCustomRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, ProductCustomRepository {

    @Query("SELECT p FROM Product p WHERE (p.status =:firstStatus OR p.status =:secondStatus) AND p.isUse=true")
    Page<Product> findMain(@Param("firstStatus") ProductStatus firstStatus, @Param("secondStatus") ProductStatus secondStatus, Pageable pageable);

}
