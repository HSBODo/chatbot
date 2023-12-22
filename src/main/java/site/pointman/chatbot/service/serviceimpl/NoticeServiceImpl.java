package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.common.ListItem;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.ListCard;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;
import site.pointman.chatbot.service.chatbot.NoticeChatBotResponseService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    NoticeRepository noticeRepository;
    ChatBotExceptionResponse chatBotExceptionResponse;
    NoticeChatBotResponseService noticeChatBotResponseService;

    public NoticeServiceImpl(NoticeRepository noticeRepository, NoticeChatBotResponseService noticeChatBotResponseService) {
        this.noticeRepository = noticeRepository;
        this.noticeChatBotResponseService = noticeChatBotResponseService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public Response addNotice(Notice notice) {
        try {
            Long noticeId = noticeRepository.save(notice);

            return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 등록하였습니다. 게시글 ID="+noticeId);
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"게시글 등록에 실패하였습니다.");
        }
    }

    @Override
    public List<Notice> getNoticeAll() {
        List<Notice> notices = noticeRepository.findByAll();
        return notices;
    }

    @Override
    public Response getNotices(boolean isChatBotRequest) {
        List<Notice> notices = noticeRepository.findByStatus(NoticeStatus.작성);


        return noticeChatBotResponseService.getNoticesSuccessChatBotResponse(notices);
    }

    @Override
    public Response getNotice(String noticeId, boolean isChatBotRequest) {
        long parseNoticeId = Long.parseLong(noticeId);

        Optional<Notice> mayBeNotice = noticeRepository.findByNoticeId(parseNoticeId);

        if(mayBeNotice.isEmpty()) return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다.");
        Notice notice = mayBeNotice.get();

       return noticeChatBotResponseService.getNoticeSuccessChatBotResponse(notice);
    }

    @Override
    public HttpResponse removeNotice(Long noticeId) {
        noticeRepository.deleteNotice(noticeId);


        return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 삭제하였습니다.");
    }
}
