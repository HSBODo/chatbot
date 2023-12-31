package site.pointman.chatbot.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.notice.NoticeDto;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
    Response addNotice(NoticeDto noticeDto);
    List<Notice> getNoticeAll();
    Page<Notice> getDefaultNotices(int pageNumber);
    Optional<Notice> getNotice(Long noticeId);
    Response removeNotice(Long noticeId);
    Response updateNotice(Long noticeId, NoticeDto noticeDto);
}
