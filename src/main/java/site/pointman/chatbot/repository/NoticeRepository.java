package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {
    Long save(Notice notice);
    List<Notice> findByStatus(NoticeStatus noticeStatus);
    List<Notice> findByAll();
    Optional<Notice> findByNoticeId(Long noticeId);
    void deleteNotice(Long noticeId);
}
