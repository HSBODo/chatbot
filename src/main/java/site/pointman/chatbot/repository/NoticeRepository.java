package site.pointman.chatbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.pointman.chatbot.domain.notice.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.repository.customrepository.NoticeCustomRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeCustomRepository {

    @Query(value = "SELECT n FROM Notice n  Join Fetch n.member WHERE (n.status =:firstStatus OR n.status =:secondStatus) AND n.isUse =:isUse ORDER BY FIELD(n.status,:firstStatus,:secondStatus), n.createDate DESC",
            countQuery = "SELECT count(n) FROM Notice n WHERE (n.status =:firstStatus OR n.status =:secondStatus) AND n.isUse =:isUse ORDER BY FIELD(n.status,:firstStatus,:secondStatus), n.createDate DESC")
    Page<Notice> findByStatusOrStatus(
            @Param("firstStatus") NoticeStatus firstNoticeStatus,
            @Param("secondStatus") NoticeStatus secondNoticeStatus,
            @Param("isUse") boolean isUse,
            Pageable pageable
    );
}
