package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.NoticeRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class NoticeRepositoryImpl implements NoticeRepository {
    private final EntityManager em;


    public NoticeRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Long save(Notice notice) {
        notice.changeStatus(NoticeStatus.작성);
        em.persist(notice);
        return notice.getId();
    }
}
