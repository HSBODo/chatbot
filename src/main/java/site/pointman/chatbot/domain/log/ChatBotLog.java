package site.pointman.chatbot.domain.log;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@Table(name = "tb_log")
@NoArgsConstructor
public class ChatBotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_key")
    private String userKey;

    @Column(name = "block_id")
    private String blockId;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "skill_id")
    private String skillId;

    @Column(name = "skill_name")
    private String skillName;

    @Lob
    @Column(name = "request_json")
    private String requestJson;

    @Lob
    @Column(name = "response_json")
    private String responseJson;

    @CreatedDate
    @Column(name = "create_date_time")
    private String createDate;

    @PrePersist
    public void onPrePersist(){
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Builder
    public ChatBotLog(String userKey, String blockId, String blockName, String skillId, String skillName, String requestJson, String responseJson, String createDate) {

        this.userKey = userKey;
        this.blockId = blockId;
        this.blockName = blockName;
        this.skillId = skillId;
        this.skillName = skillName;
        this.requestJson = requestJson;
        this.responseJson = responseJson;
        this.createDate = createDate;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }
}
