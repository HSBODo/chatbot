package site.pointman.chatbot.domain.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Block {
    private String id;
    private String name;
}
