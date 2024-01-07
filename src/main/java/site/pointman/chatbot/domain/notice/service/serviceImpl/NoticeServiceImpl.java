package site.pointman.chatbot.domain.notice.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.notice.constant.NoticeStatus;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.exception.NotFoundNotice;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.domain.notice.service.NoticeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {
    private boolean isUse = true;

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void addNotice(NoticeDto noticeDto) {
        Optional<Member> myBeMember = memberRepository.findByName(noticeDto.getWriter(),isUse);
        if (myBeMember.isEmpty()) throw  new NotFoundMember("작성자가 존재하지 않습니다.");

        Member member = myBeMember.get();

        Notice notice = noticeDto.toEntity();
        notice.changeMember(member);
        noticeRepository.save(notice);
    }

    @Override
    public List<Notice> getNoticeAll() {
        List<Notice> notices = noticeRepository.findAll();

        return notices;
    }

    @Override
    public List<NoticeDto> getNoticeDtoAll() {
        List<Notice> noticeAll = getNoticeAll();
        List<NoticeDto> noticeDtos = new ArrayList<>();

        noticeAll.forEach(notice -> {
            noticeDtos.add(notice.toDto());
        });

        return noticeDtos;
    }

    @Override
    public Page<Notice> getDefaultNotices(int pageNumber) {
        Page<Notice> notices = noticeRepository.findByStatusOrStatus(NoticeStatus.메인,NoticeStatus.작성,isUse, PageRequest.of(pageNumber,5));

        return notices;
    }

    @Override
    public Notice getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundNotice("게시글이 존재하지 않습니다."));


        return notice;
    }

    @Override
    public NoticeDto getNoticeDto(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundNotice("게시글이 존재하지 않습니다."));

        NoticeDto noticeDto = notice.toDto();
        return noticeDto;
    }

    @Transactional
    @Override
    public void removeNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    @Override
    public void updateNotice(Long noticeId, NoticeDto noticeDto) {
        Notice notice = getNotice(noticeId);

        if(Objects.nonNull(noticeDto.getType())) notice.changeType(noticeDto.getType());
        if(Objects.nonNull(noticeDto.getImageUrl())) notice.changeImageUrl(noticeDto.getImageUrl());
        if(Objects.nonNull(noticeDto.getTitle())) notice.changeTitle(noticeDto.getTitle());
        if(Objects.nonNull(noticeDto.getDescription())) notice.changeDescription(noticeDto.getDescription());
        if(Objects.nonNull(noticeDto.getButtons())) notice.changeButtons(noticeDto.getButtons());
        if(Objects.nonNull(noticeDto.getStatus())) notice.changeStatus(noticeDto.getStatus());
    }
}
