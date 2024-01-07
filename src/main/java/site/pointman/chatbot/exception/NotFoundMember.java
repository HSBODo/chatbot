package site.pointman.chatbot.exception;

public class NotFoundMember extends RuntimeException{
    public NotFoundMember() {
        super();
    }

    public NotFoundMember(String message) {
        super(message);
    }

    public NotFoundMember(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMember(Throwable cause) {
        super(cause);
    }

    protected NotFoundMember(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
