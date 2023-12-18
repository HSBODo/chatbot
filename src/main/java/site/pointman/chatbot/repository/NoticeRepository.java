package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.notice.Notice;

public interface NoticeRepository {
    Long save(Notice notice);

}
