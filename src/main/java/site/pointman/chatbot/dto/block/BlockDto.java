package site.pointman.chatbot.dto.block;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.DisplayType;
import site.pointman.chatbot.dto.kakaoui.ListCardItemDto;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class BlockDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String blockName;
    @NotBlank
    DisplayType displayType;
    @NotBlank
    BlockType blockType ;
    @NotBlank
    private BlockServiceType service;

    private String title;
    private String thumbnailImgUrl;
    private String thumbnailLink;
    private String profileImgUrl;
    private String profileNickname;
    private String description ;
    private int price;
    private int discount;
    private int discountedPrice;
    private int discountRate;
    private String currency = "won";
    private List<ListCardItemDto> listCardItemList = new ArrayList<>();
    private List<ButtonDto> buttonList = new ArrayList<>();
    private List<ButtonDto> quickButtonList = new ArrayList<>();


    @Builder
    public BlockDto(Long id, String blockName, DisplayType displayType, BlockType blockType, BlockServiceType service, String title, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String profileNickname, String description, int price, int discount, int discountedPrice, int discountRate, String currency, List<ListCardItemDto> listCardItemList, List<ButtonDto> buttonList, List<ButtonDto> quickButtonList) {
        this.id = id;
        this.blockName = blockName;
        this.displayType = displayType;
        this.blockType = blockType;
        this.service = service;
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

    public Block toEntity(){
        return Block.builder()
                .blockName(this.blockName)
                .blockType(this.blockType)
                .displayType(this.displayType)
                .service(this.service)
                .build();
    }

    public BlockDto simpleText(BlockType blockType,DisplayType displayType ,String description){
        return BlockDto.builder()
                .blockType(blockType)
                .displayType(displayType)
                .description(description)
                .build();
    }
    public BlockDto basicCard(BlockType blockType, DisplayType displayType,String title, String description, String thumbnailImgUrl,List<ButtonDto> buttonList){
        return BlockDto.builder()
                .blockType(blockType)
                .displayType(displayType)
                .title(title)
                .description(description)
                .thumbnailImgUrl(thumbnailImgUrl)
                .buttonList(buttonList)
                .build();
    }






}
