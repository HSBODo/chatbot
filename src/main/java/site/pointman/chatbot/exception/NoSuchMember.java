package site.pointman.chatbot.exception;

public class NoSuchMember extends RuntimeException{
    public NoSuchMember() {
        super();
    }

    public NoSuchMember(String message) {
        super(message);
    }

    public NoSuchMember(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMember(Throwable cause) {
        super(cause);
    }

    protected NoSuchMember(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
