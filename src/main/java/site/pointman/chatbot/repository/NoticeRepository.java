package site.pointman.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.customrepository.NoticeCustomRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeCustomRepository {

    @Override
    List<Notice> findAll();

    @Query("select n from Notice n where n.status =:firstStatus OR n.status =:secondStatus AND n.isUse =:isUse ORDER BY FIELD(n.status,:firstStatus,:secondStatus), n.createDate DESC")
    List<Notice> findByStatusOrStatus(
            @Param("firstStatus") NoticeStatus firstNoticeStatus,
            @Param("secondStatus") NoticeStatus secondNoticeStatus,
            @Param("isUse") boolean isUse
    );

    @Override
    Optional<Notice> findById(Long noticeId);

    @Override
    void deleteById(Long noticeId);
}
