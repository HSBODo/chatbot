package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.dto.notice.NoticeDto;

import java.util.List;
import java.util.Optional;

public interface NoticeCustomRepository {
    Notice updateNotice(Long noticeId, NoticeDto noticeDto);
}
