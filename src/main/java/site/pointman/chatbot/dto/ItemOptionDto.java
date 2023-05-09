package site.pointman.chatbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.domain.item.ItemOptionCategory;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ItemOptionDto {
    private Long id ;
    @NotBlank(message = "아이템코드는 필수입니다.")
    private Long itemCode;
    @NotBlank(message = "옵션명은 필수입니다.")
    private String optionName;
    @NotBlank(message = "옵션카테고리는 필수입니다.")
    private ItemOptionCategory category;
    private int optionPrice;
    @Min(0)
    private int quantity;
    @Builder
    public ItemOptionDto(Long id, Long optionCode, Long itemCode, String optionName, ItemOptionCategory category, int optionPrice) {
        this.id = id;
        this.itemCode = itemCode;
        this.optionName = optionName;
        this.category = category;
        this.optionPrice = optionPrice;
    }
    public ItemOption toEntity(){
        return ItemOption.builder()
                .itemCode(this.itemCode)
                .optionName(this.optionName)
                .optionPrice(this.optionPrice)
                .category(this.category)
                .build();
    }
}
