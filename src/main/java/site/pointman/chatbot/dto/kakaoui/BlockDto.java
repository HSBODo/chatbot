package site.pointman.chatbot.dto.kakaoui;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;

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
    public BlockDto(Long id, DisplayType displayType, BlockType blockType, String blockName, BlockServiceType service) {
        this.id = id;
        this.displayType = displayType;
        this.blockType = blockType;
        this.blockName = blockName;
        this.service = service;
    }
    public Block toEntity(){
        return Block.builder()
                .blockName(this.blockName)
                .blockType(this.blockType)
                .displayType(this.displayType)
                .service(this.service)
                .build();
    }
    public void simpleText(String description){
        this.description = description;
    }
    public void basicCard(String title, String description, String thumbnailImgUrl,List<ButtonDto> buttonList){
        this.title = title;
        this.description = description;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.buttonList = buttonList;
    }






}
