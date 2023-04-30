package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.repository.BlockRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class BlockRepositoryImpl implements BlockRepository {
    private final EntityManager em;

    public BlockRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Block save(Block block) {
        em.persist(block);
        return block;
    }

    @Override
    public Optional<Block> findByBlock(String blockCode) {
        Block findBlock = em.find(Block.class, blockCode);
        return Optional.ofNullable(findBlock);
    }
}
