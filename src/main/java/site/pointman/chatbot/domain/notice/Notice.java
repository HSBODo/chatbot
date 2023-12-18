package site.pointman.chatbot.domain.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.StringListConverter;
import site.pointman.chatbot.domain.response.property.common.Button;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "writer")
    private Member member;

    private String imageUrl;

    @Convert(converter = StringListConverter.class)
    private List<Button> buttons;

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

    public void changeStatus(NoticeStatus status) {
        this.status = status;
    }
}
