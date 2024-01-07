package site.pointman.chatbot.view.kakaochatobotview;

import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.notice.Notice;

public interface NoticeChatBotView {
    ChatBotResponse noticeListPage(Page<Notice> mainNoticePage, int pageNumber);
    ChatBotResponse noticeDetailPage(Notice notice);
}
