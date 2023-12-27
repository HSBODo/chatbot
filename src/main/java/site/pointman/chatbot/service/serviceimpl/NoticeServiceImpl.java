package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
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
    public HttpResponse addNotice(Notice notice) {
        try {
            Notice saveNotice = noticeRepository.save(notice);

            return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 등록하였습니다. 게시글 ID="+saveNotice.getId());
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"게시글 등록에 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse getNoticeAll() {
        List<Notice> notices = noticeRepository.findByAll();
        if (notices.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"게시글이 존재하지 않습니다.");

        return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 조회하였습니다.",notices);
    }

    @Override
    public HttpResponse getNotices() {
        List<Notice> notices = noticeRepository.findByStatus(NoticeStatus.작성);
        if (notices.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"게시글이 존재하지 않습니다.");

        return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 조회하였습니다.",notices);
    }

    @Override
    public HttpResponse getNotice(String noticeId) {
        long parseNoticeId = Long.parseLong(noticeId);

        Optional<Notice> mayBeNotice = noticeRepository.findByNoticeId(parseNoticeId);
        if (mayBeNotice.isEmpty()) return new HttpResponse(ApiResultCode.EXCEPTION,"게시글이 존재하지 않습니다.");
        Notice notice = mayBeNotice.get();

        return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 조회하였습니다.",notice);
    }

    @Override
    public HttpResponse removeNotice(Long noticeId) {
        noticeRepository.deleteNotice(noticeId);


        return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 삭제하였습니다.");
    }

    @Override
    public HttpResponse updateNotice(Long noticeId, Notice notice) {
        try {
            noticeRepository.updateNotice(noticeId, notice);

            return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 수정하였습니다.");
        }catch (Exception e){
            log.info("e={}",e.getMessage());
            return new HttpResponse(ApiResultCode.FAIL,"게시글 수정을 실패하였습니다.");
        }
    }
}
