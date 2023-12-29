package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.notice.NoticeDto;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    private boolean isUse = true;

    NoticeRepository noticeRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public Notice addNotice(Notice notice) {
        Notice saveNotice = noticeRepository.save(notice);

        return saveNotice;
    }

    @Override
    public List<Notice> getNoticeAll() {
        List<Notice> notices = noticeRepository.findAll();

        return notices;
    }

    @Override
    public List<Notice> getDefaultNotices() {
        List<Notice> notices = noticeRepository.findByStatusOrStatus(NoticeStatus.메인,NoticeStatus.작성,isUse);

        return notices;
    }

    @Override
    public Optional<Notice> getNotice(Long noticeId) {
        Optional<Notice> mayBeNotice = noticeRepository.findById(noticeId);

        return mayBeNotice;
    }

    @Override
    public Response removeNotice(Long noticeId) {
        try {
            noticeRepository.deleteById(noticeId);

            return new Response(ResultCode.OK,"정상적으로 게시글을 삭제하였습니다.");
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"게시글 삭제를 실패하였습니다.");
        }
    }

    @Override
    public Response updateNotice(Long noticeId, NoticeDto noticeDto) {
        try {
            noticeRepository.updateNotice(noticeId, noticeDto);

            return new Response(ResultCode.OK,"정상적으로 게시글을 수정하였습니다.");
        }catch (Exception e){
            return new Response(ResultCode.FAIL,"게시글 수정을 실패하였습니다.");
        }
    }
}
