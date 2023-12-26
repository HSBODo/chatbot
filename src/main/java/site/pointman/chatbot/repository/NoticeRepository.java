package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.custom.NoticeCustomRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeCustomRepository {

}
