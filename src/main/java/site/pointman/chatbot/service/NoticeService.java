package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.HttpResponse;

import java.util.List;

public interface NoticeService {
    HttpResponse addNotice(Notice notice);
    HttpResponse getNoticeAll();
    HttpResponse removeNotice(Long noticeId);
    HttpResponse getNotices();
    HttpResponse getNotice(String noticeId);
    HttpResponse updateNotice(Long noticeId, Notice notice);
}
