package site.pointman.chatbot.dto.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Bot
{
    private String id;
    private String name;
}
