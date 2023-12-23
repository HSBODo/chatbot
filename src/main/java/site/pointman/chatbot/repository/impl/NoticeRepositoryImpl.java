package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.NoticeRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional
public class NoticeRepositoryImpl implements NoticeRepository {
    private final EntityManager em;


    public NoticeRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override

    public Long save(Notice notice) {
        notice.changeStatus(NoticeStatus.작성);
        em.persist(notice);
        return notice.getId();
    }

    @Override
    public List<Notice> findByStatus(NoticeStatus noticeStatus) {
        return em.createQuery("select n from Notice n where n.status =:status OR n.status =:main ORDER BY FIELD(n.status,:main,:status)",Notice.class)
                .setParameter("status",noticeStatus)
                .setParameter("main",NoticeStatus.메인)
                .getResultList();
    }

    @Override
    public List<Notice> findByAll() {
        return em.createQuery("select n from Notice n ORDER BY n.createDate DESC",Notice.class)
                .getResultList();
    }

    @Override
    public Optional<Notice> findByNoticeId(Long noticeId) {
        return em.createQuery("select n from Notice n where n.status <>:status AND n.id =:id",Notice.class)
                .setParameter("id",noticeId)
                .setParameter("status",NoticeStatus.숨김)
                .getResultList().stream().findAny();
    }

    @Override
    public void deleteNotice(Long noticeId) {
        Notice notice = em.createQuery("select n from Notice n where n.id =:id", Notice.class)
                .setParameter("id", noticeId)
                .getResultList()
                .stream().findAny()
                .get();
        em.remove(notice);
    }

    @Override
    public void updateNotice(Long noticeId, Notice toNotice) {
        Notice findNotice = em.createQuery("select n from Notice n where n.id =:id", Notice.class)
                .setParameter("id", noticeId)
                .getResultList().stream().findAny().get();

        if(Objects.nonNull(toNotice.getType())) findNotice.changeType(toNotice.getType());
        if(Objects.nonNull(toNotice.getImageUrl())) findNotice.changeImageUrl(toNotice.getImageUrl());
        if(Objects.nonNull(toNotice.getTitle())) findNotice.changeTitle(toNotice.getTitle());
        if(Objects.nonNull(toNotice.getDescription())) findNotice.changeDescription(toNotice.getDescription());
        if(Objects.nonNull(toNotice.getButtons())) findNotice.changeButtons(toNotice.getButtons());
        if(Objects.nonNull(toNotice.getStatus())) findNotice.changeStatus(toNotice.getStatus());
    }
}
