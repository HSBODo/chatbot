package site.pointman.chatbot.exception;

public class NotFoundPaymentInfo extends RuntimeException{
    public NotFoundPaymentInfo() {
        super();
    }

    public NotFoundPaymentInfo(String message) {
        super(message);
    }

    public NotFoundPaymentInfo(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundPaymentInfo(Throwable cause) {
        super(cause);
    }

    protected NotFoundPaymentInfo(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
