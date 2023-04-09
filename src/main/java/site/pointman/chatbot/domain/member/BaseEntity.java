package site.pointman.chatbot.domain.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class BaseEntity {
    @Column(name = "create_time")
    private LocalDateTime createDate;
    @Column(name = "delete_time")
    private LocalDateTime lastModifiedDate;
    @Column(name = "update_time")
    private LocalDateTime deleteDate;

}
