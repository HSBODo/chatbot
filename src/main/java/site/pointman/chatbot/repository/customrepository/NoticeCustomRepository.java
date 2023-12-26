package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeCustomRepository {
    List<Notice> findByStatus(NoticeStatus noticeStatus);
    List<Notice> findByAll();
    Optional<Notice> findByNoticeId(Long noticeId);
    void deleteNotice(Long noticeId);
    void updateNotice(Long noticeId, Notice toNotice);
}
