package site.pointman.chatbot.dto.response.property.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleText {
    private String text;
}
