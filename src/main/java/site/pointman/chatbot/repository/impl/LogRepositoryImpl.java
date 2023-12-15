package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.log.Log;
import site.pointman.chatbot.repository.LogRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class LogRepositoryImpl implements LogRepository {
    private final EntityManager em;

    public LogRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Log log) {
        em.persist(log);
        return log.getId();
    }
}
