package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;

public interface NoticeService {
    Response addNotice(Notice notice);
    Response getNoticeAll();
    Response removeNotice(Long noticeId);
    Response getNotices();
    Response getNotice(String noticeId);
    Response updateNotice(Long noticeId, Notice notice);
}
