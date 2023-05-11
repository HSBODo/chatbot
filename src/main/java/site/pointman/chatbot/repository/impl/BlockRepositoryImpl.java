package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.repository.BlockRepository;
import site.pointman.chatbot.service.BlockService;

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
    public Optional<Block> findByBlock(Long blockId) {
        Block findBlock = em.find(Block.class, blockId);
        return Optional.ofNullable(findBlock);
    }

    @Override
    public Optional<Block> findByBlock(BlockServiceType blockServiceType) {
        return em.createQuery("select b from Block b where  b.service=:service", Block.class)
                .setParameter("service",blockServiceType)
                .getResultList().stream().findAny();
    }
}
