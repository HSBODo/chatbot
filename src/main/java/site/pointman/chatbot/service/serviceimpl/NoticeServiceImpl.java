package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.notice.NoticeDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    private boolean isUse = true;

    NoticeRepository noticeRepository;
    MemberRepository memberRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository, MemberRepository memberRepository) {
        this.noticeRepository = noticeRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Response addNotice(NoticeDto noticeDto) {
        Optional<Member> myBeMember = memberRepository.findByName(noticeDto.getWriter(),isUse);
        if (myBeMember.isEmpty()) return new Response(ResultCode.FAIL,"작성자가 존재하지 않습니다.");

        Member member = myBeMember.get();


        Notice notice = noticeDto.toEntity();
        notice.changeMember(member);

        Notice saveNotice = noticeRepository.save(notice);

        return new Response(ResultCode.OK,"성공적으로 글을 작서하였습니다.","게시글 번호= "+saveNotice.getId());
    }

    @Override
    public List<Notice> getNoticeAll() {
        List<Notice> notices = noticeRepository.findAll();

        return notices;
    }

    @Override
    public Page<Notice> getDefaultNotices(int pageNumber) {
        Page<Notice> notices = noticeRepository.findByStatusOrStatus(NoticeStatus.메인,NoticeStatus.작성,isUse, PageRequest.of(pageNumber,5));

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
