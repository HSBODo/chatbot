package site.pointman.chatbot.repository.customrepository.impl;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;
import site.pointman.chatbot.repository.customrepository.NoticeCustomRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Objects;

@Transactional
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {
    private final EntityManager em;

    public NoticeCustomRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Notice updateNotice(Long noticeId, NoticeDto noticeDto) {
        Notice findNotice = em.createQuery("select n from Notice n where n.id =:id", Notice.class)
                .setParameter("id", noticeId)
                .getResultList().stream().findAny().get();

        if(Objects.nonNull(noticeDto.getType())) findNotice.changeType(noticeDto.getType());
        if(Objects.nonNull(noticeDto.getImageUrl())) findNotice.changeImageUrl(noticeDto.getImageUrl());
        if(Objects.nonNull(noticeDto.getTitle())) findNotice.changeTitle(noticeDto.getTitle());
        if(Objects.nonNull(noticeDto.getDescription())) findNotice.changeDescription(noticeDto.getDescription());
        if(Objects.nonNull(noticeDto.getButtons())) findNotice.changeButtons(noticeDto.getButtons());
        if(Objects.nonNull(noticeDto.getStatus())) findNotice.changeStatus(noticeDto.getStatus());
        return findNotice;
    }
}
