package site.pointman.chatbot.repository;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;

import java.util.List;

public interface NoticeRepository {
    Long save(Notice notice);
    List<Notice> findAll(NoticeStatus noticeStatus);

}
