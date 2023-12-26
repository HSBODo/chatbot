package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.customrepository.ProductCustomRepository;

public interface ProductRepository extends JpaRepository<Product,Long>, ProductCustomRepository {

}
