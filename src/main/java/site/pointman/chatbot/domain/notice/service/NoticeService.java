package site.pointman.chatbot.domain.notice.service;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
    void addNotice(NoticeDto noticeDto);
    List<Notice> getNoticeAll();
    List<NoticeDto> getNoticeDtoAll();
    Page<Notice> getDefaultNotices(int pageNumber);
    Notice getNotice(Long noticeId);
    NoticeDto getNoticeDto(Long noticeId);
    void removeNotice(Long noticeId);
    void updateNotice(Long noticeId, NoticeDto noticeDto);
}
