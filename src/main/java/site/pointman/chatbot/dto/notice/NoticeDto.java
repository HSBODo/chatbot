package site.pointman.chatbot.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.notice.ButtonListConverter;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.notice.NoticeTypeEnumConverter;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.utill.CustomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class NoticeDto {

    private Long id ;
    private NoticeType type;
    private String title;
    private String description;
    private String writer;
    private String imageUrl;
    private List<Button> buttons = new ArrayList<>();
    private NoticeStatus status;
    private String createDate;
    private String modificationDate;

    @Builder
    public NoticeDto(Long id, NoticeType type, String title, String description, String writer, String imageUrl, List<Button> buttons, NoticeStatus status, String createDate, String modificationDate) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.buttons = buttons;
        this.status = status;
        this.createDate = createDate;
        this.modificationDate = modificationDate;
    }

    public Notice toEntity(){
        return Notice.builder()
                .type(type)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .buttons(buttons)
                .status(status)
                .build();
    }
}
