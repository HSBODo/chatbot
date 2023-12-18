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

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Slf4j
class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void save() {
        //give
        Member member = Member.builder()
                .userKey("QFJSyeIZbO77")
                .build();

        List<Button> buttons = new ArrayList<>();
        Button button = new Button("btnName", ButtonAction.블럭이동, "asdasd", ButtonParamKey.productId, "asdasdasd");
        buttons.add(button);

        Notice notice = Notice.builder()
                .member(member)
                .type(NoticeType.TEXT_CARD)
                .title("제목 테스트")
                .description("설명 테스트")
                .imageUrl("https://")
                .buttons(buttons)
                .build();

        //when
        Long id = noticeRepository.save(notice);

        //then
        Assertions.assertThat(id).isNotEqualTo(0);
    }


    @Test
    void findByStatus() {
        //give
        NoticeStatus status = NoticeStatus.작성;

        //when
        List<Notice> notices = noticeRepository.findByStatus(status);

        //then
        Assertions.assertThat(notices.size()).isNotZero();

        notices.forEach(notice -> {
            log.info("title={}",notice.getTitle());
            Assertions.assertThat(notice.getStatus()).isIn(status,NoticeStatus.메인);
        });
    }

    @Test
    void findByNoticeId() {
        //give
        Long id = 10L;

        //when
        Notice notice = noticeRepository.findByNoticeId(id).get();

        //then
        Assertions.assertThat(notice.getId()).isEqualTo(id);
    }
}