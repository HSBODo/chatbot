package site.pointman.chatbot.domain.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime lastModifiedDate;

    @Column(name = "delete_time")
    private LocalDateTime deleteDate;
}
