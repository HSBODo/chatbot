package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.dto.notice.NoticeDto;

public interface NoticeCustomRepository {
    Notice updateNotice(Long noticeId, NoticeDto noticeDto);
}
