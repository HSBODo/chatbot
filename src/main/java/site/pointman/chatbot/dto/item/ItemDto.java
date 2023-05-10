package site.pointman.chatbot.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.item.Item;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private Long idx ;
    @NotBlank(message = "상품코드는 필수입니다.")
    private Long itemCode;
    private String description; //최대 40자
    private String thumbnailImgUrl;
    private String thumbnailLink;
    private String profileImgUrl;
    @NotBlank(message = "상품이름은 필수입니다.")
    private String ProfileNickname;
    @NotBlank(message = "상품의 원가격은 필수입니다.")
    private int price;
    private int discount;
    @NotBlank(message = "상품의 최종가격은 필수입니다.")
    private int discountedPrice;
    @Range(min = 0,max = 100)
    private int discountRate;
    private String currency;
    private String is_display;
    @NotBlank(message = "상품의 수량은 필수입니다.")
    private Long total_quantity;

    @Builder
    public ItemDto(Long idx, Long itemCode, String description, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String profileNickname, int price, int discount, int discountedPrice, int discountRate, String currency, String is_display, Long total_quantity) {
        this.idx = idx;
        this.itemCode = itemCode;
        this.description = description;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.thumbnailLink = thumbnailLink;
        this.profileImgUrl = profileImgUrl;
        this.ProfileNickname = profileNickname;
        this.price = price;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
        this.discountRate = discountRate;
        this.currency = currency;
        this.is_display = is_display;
        this.total_quantity = total_quantity;
    }
    public Item toEntity(){
        return Item.builder()
                .itemCode(this.itemCode)
                .description(this.description)
                .thumbnailImgUrl(this.thumbnailImgUrl)
                .thumbnailLink(this.thumbnailLink)
                .profileNickname(this.ProfileNickname)
                .profileImgUrl(this.profileImgUrl)
                .price(this.price)
                .discount(this.discount)
                .discountRate(this.discountRate)
                .discountedPrice(this.discountedPrice)
                .currency(this.currency)
                .is_display(this.is_display)
                .total_quantity(this.total_quantity)
                .build();
    }
}
