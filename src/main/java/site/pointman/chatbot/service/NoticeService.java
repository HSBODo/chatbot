package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;

public interface NoticeService {
    Response addNotice(Notice notice);
    Response getNotices(boolean isChatBotRequest);
    Response getNotice(String noticeId, boolean isChatBotRequest);
}
