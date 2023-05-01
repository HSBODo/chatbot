package site.pointman.chatbot.domain.item;

import lombok.Builder;
import site.pointman.chatbot.dto.ItemDto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Option {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;
    @Id
    @Column(name = "item_code",nullable = false)
    private Long itemCode;
    private String description; //최대 40자
    private String thumbnailImgUrl;
    private String thumbnailLink;

    private String profileImgUrl;
    @Column(name = "item_name",nullable = false)
    private String ProfileNickname;
    @Column(name = "original_price",nullable = false)
    private int price;
    private int discount;
    @Column(name = "final_price",nullable = false)
    private int discountedPrice;
    private int discountRate;
    private String currency;
    private String is_display;
    @Column(nullable = false)
    private Long total_quantity;

    @Builder
    public Option(Long idx, Long itemCode, String description, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String profileNickname, int price, int discount, int discountedPrice, int discountRate, String currency, String is_display, Long total_quantity) {
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

    public ItemDto toItemDto(){
        return ItemDto.builder()
                .idx(this.idx)
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
