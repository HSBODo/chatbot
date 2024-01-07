package site.pointman.chatbot.exception;

public class NotFoundOrder extends RuntimeException{

    public NotFoundOrder() {
        super();
    }

    public NotFoundOrder(String message) {
        super(message);
    }

    public NotFoundOrder(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundOrder(Throwable cause) {
        super(cause);
    }

    protected NotFoundOrder(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
