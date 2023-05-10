package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.dto.block.BlockDto;
import site.pointman.chatbot.dto.kakaoui.DisplayType;

import java.util.Optional;

@Slf4j
@SpringBootTest
class BlockRepositoryTest {

    @Autowired
    BlockRepository blockRepository;

    @Test
    @Commit
    void save() {
        BlockDto blockDto = BlockDto.builder()
                .blockName("테스트")
                .blockType(BlockType.basicCard)
                .displayType(DisplayType.basic)
                .service(BlockServiceType.옵션)
                .build();
        Block block = blockDto.toEntity();
        blockRepository.save(block);
    }
    @Test
    void findByBlock() {
        BlockDto blockDto = BlockDto.builder()
                .id(5L)
                .blockName("테스트")
                .blockType(BlockType.basicCard)
                .displayType(DisplayType.basic)
                .service(BlockServiceType.옵션)
                .build();
        Optional<Block> maybeBlock = blockRepository.findByBlock(blockDto.getId());
        if(maybeBlock.isEmpty()) throw new NullPointerException("블럭이 존제하지 않습니다");
        Block block = maybeBlock.get();
        Assertions.assertThat(block.getId()).isEqualTo(blockDto.getId());
    }
}