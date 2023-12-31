package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface NoticeChatBotView {
    ChatBotResponse noticeListPage(int pageNumber);
    ChatBotResponse noticeDetailPage(String noticeId);
}
