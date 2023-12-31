package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.response.ChatBotResponse;

public interface NoticeChatBotView {
    ChatBotResponse getNoticesSuccessChatBotResponse();
    ChatBotResponse getNoticeSuccessChatBotResponse(String noticeId);
}
