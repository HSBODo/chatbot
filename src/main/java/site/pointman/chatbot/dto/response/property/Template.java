package site.pointman.chatbot.dto.response.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButton;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Template {
    private List<Component> outputs;
    private List<QuickReplyButton> quickReplies;

    public Template() {
        this.outputs = new ArrayList<>();
    }
}
