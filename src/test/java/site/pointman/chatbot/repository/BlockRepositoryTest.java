package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.vo.kakaoui.DisplayType;

@Slf4j
@SpringBootTest
class BlockRepositoryTest {

    @Autowired
    BlockRepository blockRepository;

    @Test
    @Commit
    void save() {
        BlockDto blockDto = BlockDto.builder()
                .blockCode("123")
                .blockName("테스트")
                .blockType(BlockType.basicCard)
                .displayType(DisplayType.basic)
                .service("테스트")
                .build();
        Block block = blockDto.toEntity();
        blockRepository.save(block);
    }
}