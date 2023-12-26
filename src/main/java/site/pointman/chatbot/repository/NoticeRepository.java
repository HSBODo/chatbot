package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.customrepository.NoticeCustomRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeCustomRepository {

}
