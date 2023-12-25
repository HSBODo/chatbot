package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;

import java.util.List;

public interface NoticeService {
    Object addNotice(Notice notice);
    List<Notice> getNoticeAll();
    Object removeNotice(Long noticeId);
    Object getNotices(boolean isChatBotRequest);
    Object getNotice(String noticeId, boolean isChatBotRequest);
    Object updateNotice(Long noticeId, Notice notice);
}
