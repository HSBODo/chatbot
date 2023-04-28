package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.repository.BlockRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
}
