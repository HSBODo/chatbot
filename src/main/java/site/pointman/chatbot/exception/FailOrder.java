package site.pointman.chatbot.exception;

public class FailOrder extends RuntimeException{
    public FailOrder() {
        super();
    }

    public FailOrder(String message) {
        super(message);
    }

    public FailOrder(String message, Throwable cause) {
        super(message, cause);
    }

    public FailOrder(Throwable cause) {
        super(cause);
    }

    protected FailOrder(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
