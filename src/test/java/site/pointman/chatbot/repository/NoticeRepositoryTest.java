package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.constant.ButtonAction;
import site.pointman.chatbot.constant.ButtonParamKey;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.dto.notice.NoticeDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@SpringBootTest
class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;


    @Test
    void findAll() {


        List<Notice> notices = noticeRepository.findAll();

        Assertions.assertThat(notices.size()).isNotZero();
    }

    @Test
    void findByStatusOrStatus() {
        NoticeStatus firstStatus = NoticeStatus.메인;
        NoticeStatus secondStatus = NoticeStatus.작성;
        boolean isUse = true;

        List<Notice> notices = noticeRepository.findByStatusOrStatus(firstStatus, secondStatus, isUse);

        notices.forEach(notice -> {
            Assertions.assertThat(notice.getStatus()).isIn(firstStatus,secondStatus);
            Assertions.assertThat(notice.isUse()).isEqualTo(isUse);
        });

    }

    @Test
    void findById() {
        Long noticeId =21L;

        Optional<Notice> myBeNotice = noticeRepository.findById(noticeId);

        Assertions.assertThat(myBeNotice).isNotEmpty();
        Assertions.assertThat(myBeNotice.get().getId()).isEqualTo(noticeId);
    }

    @Test
    void deleteById() {
        Long noticeId =21L;

        noticeRepository.deleteById(noticeId);
    }

    @Test
    void updateNotice() {
        Long noticeId =21L;

        NoticeDto noticeDto = NoticeDto.builder()
                .title("제목변경")
                .description("본문 변경")
                .build();

        Notice notice = noticeRepository.updateNotice(noticeId, noticeDto);

        Assertions.assertThat(notice.getId()).isEqualTo(noticeId);
        Assertions.assertThat(notice.getTitle()).isEqualTo(noticeDto.getTitle());
        Assertions.assertThat(notice.getDescription()).isEqualTo(noticeDto.getDescription());
    }
}