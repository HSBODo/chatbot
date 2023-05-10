package site.pointman.chatbot.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.dto.item.ItemOptionDto;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_item_option")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class ItemOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    @Column(name = "item_code",nullable = false)
    private Long itemCode;
    @Column(nullable = false)
    private String optionName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemOptionCategory category;
    @ColumnDefault("0")
    private int optionPrice;
    @ColumnDefault("0")
    private int quantity;

    @Builder
    public ItemOption(Long id, Long itemCode, String optionName, ItemOptionCategory category, int optionPrice, int quantity) {
        this.id = id;
        this.itemCode = itemCode;
        this.optionName = optionName;
        this.category = category;
        this.optionPrice = optionPrice;
        this.quantity = quantity;
    }

    public ItemOptionDto toItemOptionDto(){
        return ItemOptionDto.builder()
                .id(this.id)
                .itemCode(this.itemCode)
                .optionName(this.optionName)
                .optionPrice(this.optionPrice)
                .category(this.category)

                .build();
    }
}
