package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.Response;

import java.util.List;

public interface NoticeService {
    Response addNotice(Notice notice);
    List<Notice> getNoticeAll();
    HttpResponse removeNotice(Long noticeId);
    Response getNotices(boolean isChatBotRequest);
    Response getNotice(String noticeId, boolean isChatBotRequest);
}
