package site.pointman.chatbot.dto.request.propery;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserRequest {
    private String timeZone;
    private Params params ;
    private Block block ;
    private String utterance;
    private String lang;
    private User user ;
}
