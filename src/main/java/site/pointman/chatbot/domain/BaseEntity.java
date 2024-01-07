package site.pointman.chatbot.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_date_time")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date_time")
    private LocalDateTime lastModifiedDate;

    private boolean isUse = true;

    @PrePersist
    public void onPrePersist(){
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = this.createDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.lastModifiedDate = LocalDateTime.now();
    }

    public String getCreateDate() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getLastModifiedDate() {
        return lastModifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public boolean getIsUse() {
        return isUse;
    }
    protected void delete(){
        this.isUse = false;
    }
}
