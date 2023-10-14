package site.pointman.chatbot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.dto.request.propery.*;

import java.util.List;

@Getter
public class RequestDto {
    private Intent intent;
    private UserRequest userRequest;
    private Bot bot;
    private Action action;
    private List<Context> contexts;

    public String getUserKey(){
        return this.userRequest.getUser().getProperties().getPlusfriendUserKey();
    }
    public String getEnterName(){
        return this.action.getParams().getEnterName();
    }
    public String getEnterPhone(){
        return this.action.getParams().getEnterPhone();
    }
    public String getEnterAccount(){
        return this.action.getParams().getEnterAccount();
    }
}
