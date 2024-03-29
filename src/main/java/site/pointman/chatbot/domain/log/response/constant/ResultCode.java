package site.pointman.chatbot.domain.log.response.constant;

public enum ResultCode {
    OK(200,"성공"),
    FAIL(0,"실패"),
    EXCEPTION(1,"예외")
    ;

    private final int value;
    private final String reason;

    ResultCode(int value, String reason) {
        this.value = value;
        this.reason = reason;
    }

    public int getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }
}
