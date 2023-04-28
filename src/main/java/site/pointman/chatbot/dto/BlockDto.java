package site.pointman.chatbot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.vo.kakaoui.DisplayType;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class BlockDto extends BaseEntity {
    private Long id;
    @NotBlank
    private String blockCode;
    @NotBlank
    private String blockName;
    @NotBlank
    private BlockType blockType;
    @NotBlank
    private DisplayType displayType;
    private String service;

    @Builder
    public BlockDto(Long id, String blockCode, String blockName, BlockType blockType, DisplayType displayType, String service) {
        this.id = id;
        this.blockCode = blockCode;
        this.blockName = blockName;
        this.blockType = blockType;
        this.displayType = displayType;
        this.service = service;
    }
    public Block toEntity(){
        return Block.builder()
                .blockCode(this.blockCode)
                .blockName(this.blockName)
                .blockType(this.blockType)
                .displayType(this.displayType)
                .service(this.service)
                .build();
    }
}
