package site.pointman.chatbot.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.product.converter.StringListConverter;

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

    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls;

    @Builder
    public ProductImage(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
