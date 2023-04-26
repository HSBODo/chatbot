package site.pointman.chatbot.domain.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_item")
@NoArgsConstructor
public class Item extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;
    @Id
    @Column(name = "item_code")
    private Long itemCode;
    private String description; //최대 40자
    private String thumbnailImgUrl;
    private String thumbnailLink;
    private String profileImgUrl;
    private String ProfileNickname;
    private int price;
    private int discount;
    private int discountedPrice;
    private int discountRate;
    private String currency = "won";
    private String is_display;
    private Long total_quantity;
    @Builder
    public Item(Long idx, Long itemCode, String description, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String profileNickname, int price, int discount, int discountedPrice, int discountRate, String currency, String is_display, Long total_quantity) {
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
    public void changeQuantity(Long quantity){
        this.total_quantity = quantity;
    }
}
