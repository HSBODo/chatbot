package site.pointman.chatbot.domain.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.StringListConverter;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.dto.notice.NoticeDto;
import site.pointman.chatbot.utill.CustomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_notice")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotNull
    @Convert(converter = NoticeTypeEnumConverter.class)
    private NoticeType type;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member member;

    private String imageUrl = "https://pbs.twimg.com/media/FVbk8XaaMAA2ux_?format=jpg&name=small";

    @Convert(converter = ButtonListConverter.class)
    private List<Button> buttons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    @Builder
    public Notice(Long id, NoticeType type, String title, String description, Member member, String imageUrl, List<Button> buttons, NoticeStatus status) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.member = member;
        this.imageUrl = imageUrl;
        this.buttons = buttons;
        this.status = status;
    }

    public NoticeDto toDto(){
        return NoticeDto.builder()
                .id(id)
                .writer(member.getName())
                .type(type)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .buttons(buttons)
                .status(status)
                .createDate(getCreateDate())
                .modificationDate(getLastModifiedDate())
                .build();
    }

    public void changeStatus(NoticeStatus status) {
        this.status = status;
    }

    public void changeType(NoticeType type){
        this.type = type;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeMember(Member member){
        this.member = member;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void changeImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void changeButtons(List<Button> buttons){
        this.buttons = buttons;
    }

    public String getDescriptionTypeOfChatBot(){
        StringBuilder formatDescription = new StringBuilder();
        formatDescription
                .append("작성자: "+member.getName())
                .append("\n\n")
                .append(description)
                .append("\n\n")
                .append("등록일자: " + CustomStringUtils.dateFormat(getCreateDate(), "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd"));
        return formatDescription.toString();
    }

}
