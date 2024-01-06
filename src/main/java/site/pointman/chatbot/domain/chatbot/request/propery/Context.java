package site.pointman.chatbot.domain.chatbot.request.propery;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Context {
    private String name;
    private int lifespan;
    private int ttl;
    private Map<String, ContextParamValue> params;
}
