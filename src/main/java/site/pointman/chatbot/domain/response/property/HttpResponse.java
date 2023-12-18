package site.pointman.chatbot.domain.response.property;

import lombok.Getter;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.response.Response;


@Getter
public class HttpResponse extends Response {
    private int code;
    private Object message;

    public HttpResponse(ApiResultCode code, Object message) {
        this.code = code.getValue();
        this.message = message;
    }
}
