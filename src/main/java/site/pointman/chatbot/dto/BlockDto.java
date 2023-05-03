package site.pointman.chatbot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
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
    private String blockName;
    @NotBlank
    private BlockType blockType;
    @NotBlank
    private DisplayType displayType;
    private BlockServiceType service;

    @Builder
    public BlockDto(Long id, String blockName, BlockType blockType, DisplayType displayType, BlockServiceType service) {
        this.id = id;
        this.blockName = blockName;
        this.blockType = blockType;
        this.displayType = displayType;
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
}
