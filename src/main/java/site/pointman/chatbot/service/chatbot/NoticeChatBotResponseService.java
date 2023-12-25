package site.pointman.chatbot.service.chatbot;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotResponse;

import java.util.List;

public interface NoticeChatBotResponseService {
    ChatBotResponse getNoticesSuccessChatBotResponse(List<Notice> notices);
    ChatBotResponse getNoticeSuccessChatBotResponse(Notice notice);
}
