package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.block.Block;

import java.util.Optional;

public interface BlockRepository {

     Block save(Block block);
     Optional<Block> findByBlock(String blockCode);
}
