package site.pointman.chatbot.domain.log.response;

import lombok.Getter;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;


@Getter
public class Response<T> {
    private int code;
    private String message;
    private T data;

    public Response(ResultCode code, String message, T data) {
        this.code = code.getValue();
        this.message = message;
        this.data = data;
    }

    public Response(ResultCode code, String message) {
        this.code = code.getValue();
        this.message = message;
    }
}
