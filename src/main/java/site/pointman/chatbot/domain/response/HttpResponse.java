package site.pointman.chatbot.domain.response;

import lombok.Getter;
import site.pointman.chatbot.constant.ApiResultCode;


@Getter
public class HttpResponse {
    private int code;
    private String message;
    private Object result;

    public HttpResponse(ApiResultCode code, String message, Object result) {
        this.code = code.getValue();
        this.message = message;
        this.result = result;
    }

    public HttpResponse(ApiResultCode code, String message) {
        this.code = code.getValue();
        this.message = message;
    }
}
