package site.pointman.chatbot.domain.chatbot.response.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.chatbot.response.property.common.Button;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Template {
    private List<Component> outputs;
    private List<Button> quickReplies;

    public Template() {
        this.outputs = new ArrayList<>();
        this.quickReplies = new ArrayList<>();
    }
}
