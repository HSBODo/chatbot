package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.service.BlockService;

import java.util.Optional;

public interface BlockRepository {

     Block save(Block block);
     Optional<Block> findByBlock(Long blockId);
     Optional<Block> findByBlock(BlockServiceType blockServiceType);
}
