package site.pointman.chatbot.service.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_date_time")
    private String createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date_time")
    private String lastModifiedDate;

    @Column(name = "is_use",nullable = false)
    @ColumnDefault("'Y'")
    private String isUse = "Y";

    protected void changeIsUse(String isUse) {
        this.isUse = isUse;
    }

    @PrePersist
    public void onPrePersist(){
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModifiedDate = this.createDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.lastModifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
