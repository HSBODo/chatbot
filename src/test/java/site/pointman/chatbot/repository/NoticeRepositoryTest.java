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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Slf4j
class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    @Transactional
    void save() {
        //give
        Member member = Member.builder()
                .userKey("QFJSyeIZbO77")
                .build();

        List<Button> buttons = new ArrayList<>();
        Button button = new Button("문의하기", ButtonAction.전화연결, "01000000000");
        buttons.add(button);

        Notice notice = Notice.builder()
                .member(member)
                .type(NoticeType.BASIC_CARD)
                .title("1차 베타 테스트 오픈")
                .description("1차 베타 테스가 오픈하였습니다.\n" +
                        "많은 이용 부탁드리며\n" +
                        "오류가 발생시 문의해주시면 감사하겠습니다.")
                .imageUrl("https://it.chosun.com/news/photo/202111/2021110801782_1.jpg")
                .buttons(buttons)
                .status(NoticeStatus.메인)
                .build();

        //when
        Notice saveNotice = noticeRepository.save(notice);


        //then
        Assertions.assertThat(saveNotice.getId()).isNotEqualTo(0);
    }


    @Test
    @Transactional
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
    @Transactional
    void findByNoticeId() {
        //give
        Long id = 16L;

        //when
        Notice notice = noticeRepository.findByNoticeId(id).get();

        //then
        Assertions.assertThat(notice.getId()).isEqualTo(id);
    }
}