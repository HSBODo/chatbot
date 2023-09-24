package site.pointman.chatbot.dto.response.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@JsonInclude()
public class Context {
    private String name;
    private int lifeSpan;
    private Map<String,String> params;
}
