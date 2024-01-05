package site.pointman.chatbot.domain.chatbot.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Intent {
    private String id;
    private String name;
}
