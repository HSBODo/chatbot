package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.property.common.Button;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class NoticeRepositoryTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void insert() {
        Member member = Member.builder()
                .userKey("QFJSyeIZbO77")
                .name("관리자입니다")
                .phoneNumber("01000005555")
                .memberRole(MemberRole.ADMIN)
                .build();

        List<Button> buttons = new ArrayList<>();
        Button button = new Button();
        button.createBlockButton("asd","asd");
        buttons.add(button);

        Notice notice = Notice.builder()
                .member(member)
                .type(NoticeType.TEXT_CARD)
                .title("제목 테스트")
                .description("설명 테스트")
                .imageUrl("https://")
                .buttons(buttons)
                .build();

        Long id = noticeRepository.save(notice);
        log.info("id={}",id);
    }
}