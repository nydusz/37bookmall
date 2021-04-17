package sanqibookmall.common;

public class bookException extends RuntimeException {

    public bookException() {
    }

    public bookException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new bookException(message);
    }

}
