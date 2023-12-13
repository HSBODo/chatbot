package site.pointman.chatbot.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "tb_product_image")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class ProductImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product ;
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrl;

    @Builder
    public ProductImage(Product product, List<String> imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }
}
