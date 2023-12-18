package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService {

    NoticeRepository noticeRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
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
}
