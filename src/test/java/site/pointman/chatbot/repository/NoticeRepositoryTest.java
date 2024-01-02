package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import site.pointman.chatbot.constant.notice.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;

import javax.transaction.Transactional;
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

        notices.forEach(notice -> {
            log.info("{}",notice.getTitle());
        });
    }

    @Test
    void findByStatusOrStatus() {
        NoticeStatus firstStatus = NoticeStatus.메인;
        NoticeStatus secondStatus = NoticeStatus.작성;
        boolean isUse = true;

        Page<Notice> notices = noticeRepository.findByStatusOrStatus(firstStatus, secondStatus, isUse, PageRequest.of(0,5));

        notices.getContent().forEach(notice -> {
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