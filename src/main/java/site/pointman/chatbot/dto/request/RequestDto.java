package site.pointman.chatbot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.dto.request.propery.*;

import java.util.List;

@Getter
@JsonInclude()
public class RequestDto {
    private Intent intent;
    private UserRequest userRequest;
    private Bot bot;
    private Action action;
    private List<Context> contexts;

}
