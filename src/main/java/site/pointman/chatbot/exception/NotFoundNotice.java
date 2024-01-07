package site.pointman.chatbot.exception;

public class NotFoundNotice extends RuntimeException{
    public NotFoundNotice() {
        super();
    }

    public NotFoundNotice(String message) {
        super(message);
    }

    public NotFoundNotice(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundNotice(Throwable cause) {
        super(cause);
    }

    protected NotFoundNotice(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
