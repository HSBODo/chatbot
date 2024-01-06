package site.pointman.chatbot.domain.chatbot.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContextParamValue {
    private String value;
    private String resolvedValue;
}
