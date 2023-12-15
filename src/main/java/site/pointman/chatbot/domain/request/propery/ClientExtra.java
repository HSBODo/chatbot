package site.pointman.chatbot.domain.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.domain.response.property.common.Extra;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientExtra {
    private Map<String,String> extra;
    private String productId;
}
