package site.pointman.chatbot.domain.log.response;

import lombok.Getter;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;


@Getter
public class Response {
    private int code;
    private String message;
    private Object data;

    public Response(ResultCode code, String message, Object data) {
        this.code = code.getValue();
        this.message = message;
        this.data = data;
    }

    public Response(ResultCode code, String message) {
        this.code = code.getValue();
        this.message = message;
    }
}
