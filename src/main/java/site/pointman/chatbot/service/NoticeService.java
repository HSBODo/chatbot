package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.notice.NoticeDto;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
    Notice addNotice(Notice notice);
    List<Notice> getNoticeAll();
    List<Notice> getDefaultNotices();
    Optional<Notice> getNotice(Long noticeId);
    Response removeNotice(Long noticeId);
    Response updateNotice(Long noticeId, NoticeDto noticeDto);
}
