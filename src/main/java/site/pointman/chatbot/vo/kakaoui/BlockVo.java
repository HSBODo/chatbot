package site.pointman.chatbot.vo.kakaoui;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.block.BlockType;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class BlockVo {
    DisplayType displayType ;
    BlockType blockType ;
    private String title = "";
    private String thumbnailImgUrl="";
    private String thumbnailLink="";
    private String profileImgUrl="";
    private String profileNickname="";
    private String description ="";
    private int price = 0;
    private int discount = 0;
    private int discountedPrice = 0;
    private int discountRate = 0;
    private String currency = "won";
    private List<ListCardItemVo> listCardItemList = new ArrayList<>();
    private List<ButtonVo> buttonList = new ArrayList<>();
    private List<ButtonVo> quickButtonList = new ArrayList<>();
    @Builder

    public BlockVo(DisplayType displayType, BlockType blockType, String title, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String profileNickname, String description, int price, int discount, int discountedPrice, int discountRate, String currency, List<ListCardItemVo> listCardItemList, List<ButtonVo> buttonList, List<ButtonVo> quickButtonList) {
        this.displayType = displayType;
        this.blockType = blockType;
        this.title = title;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.thumbnailLink = thumbnailLink;
        this.profileImgUrl = profileImgUrl;
        this.profileNickname = profileNickname;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
        this.discountRate = discountRate;
        this.currency = currency;
        this.listCardItemList = listCardItemList;
        this.buttonList = buttonList;
        this.quickButtonList = quickButtonList;
    }
}
