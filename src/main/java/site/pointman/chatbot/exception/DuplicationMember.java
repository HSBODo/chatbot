package site.pointman.chatbot.exception;

public class DuplicationMember extends RuntimeException{
    public DuplicationMember() {
    }

    public DuplicationMember(String message) {
        super(message);
    }

    public DuplicationMember(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationMember(Throwable cause) {
        super(cause);
    }

    public DuplicationMember(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
