package site.pointman.chatbot.domain.block;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.vo.kakaoui.DisplayType;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "tb_block")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Block extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    @Column(nullable = false)
    private String blockCode;
    @Column(nullable = false)
    private String blockName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BlockType blockType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DisplayType displayType;
    private String service;
    @Builder
    public Block(Long id, String blockCode, String blockName, BlockType blockType, DisplayType displayType, String service) {
        this.id = id;
        this.blockCode = blockCode;
        this.blockName = blockName;
        this.blockType = blockType;
        this.displayType = displayType;
        this.service = service;
    }

    public BlockDto toBlockDto(){
        return BlockDto.builder()
                .id(this.id)
                .blockCode(this.blockCode)
                .blockName(this.blockName)
                .blockType(this.blockType)
                .displayType(this.displayType)
                .service(this.service)
                .build();
    }
}
