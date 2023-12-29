package site.pointman.chatbot.domain.response;

import lombok.Getter;
import site.pointman.chatbot.constant.ResultCode;


@Getter
public class Response {
    private int code;
    private String message;
    private Object result;

    public Response(ResultCode code, String message, Object result) {
        this.code = code.getValue();
        this.message = message;
        this.result = result;
    }

    public Response(ResultCode code, String message) {
        this.code = code.getValue();
        this.message = message;
    }
}
