package site.pointman.chatbot.dto.request.propery;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Action {
    private String name;
    private ClientExtra clientExtra;
    private Params params;
    private String id;
    private DetailParams detailParams;
}
