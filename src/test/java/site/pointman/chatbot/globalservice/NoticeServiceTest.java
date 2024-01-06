package site.pointman.chatbot.globalservice;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.notice.constant.NoticeType;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.notice.service.NoticeService;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;

    private Long noticeId = 21L;


    @Test
    void getNotice() {
        Optional<Notice> mayBeNotice = noticeService.getNotice(noticeId);
        Notice notice = mayBeNotice.get();

        Assertions.assertThat(notice.getId()).isEqualTo(noticeId);
    }

    @Test
    void addNotice() {
        //give
        NoticeDto noticeDto = NoticeDto.builder()
                .writer("라이언")
                .type(NoticeType.TEXT_CARD)
                .title("테스트")
                .description("테스트중")
                .build();
        //when
        Response response = noticeService.addNotice(noticeDto);


        //then
        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK);

    }

    @Test
    void getNoticeAll() {

        List<Notice> noticeAll = noticeService.getNoticeAll();

        Assertions.assertThat(noticeAll.size()).isNotZero();
    }

    @Test
    void removeNotice() {
        Response response = noticeService.removeNotice(noticeId);

        Assertions.assertThat(response.getCode()).isEqualTo(200);
    }

    @Test
    void updateNotice() {
        NoticeDto noticeDto = NoticeDto.builder()
                .title("제목변경")
                .description("본문 변경")
                .build();

        Response response = noticeService.updateNotice(noticeId, noticeDto);

        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());

    }
}