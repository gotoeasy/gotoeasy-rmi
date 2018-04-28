package top.gotoeasy.framework.rmi.exception;

/**
 * RMI模块异常
 * 
 * @since 2018/03
 * @author 青松
 */
public class RmiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     */
    public RmiException() {
        super();
    }

    /**
     * 构造方法
     * 
     * @param message 消息
     */
    public RmiException(String message) {
        super(message);
    }

    /**
     * 构造方法
     * 
     * @param message 消息
     * @param cause 异常
     */
    public RmiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法
     * 
     * @param cause 异常
     */
    public RmiException(Throwable cause) {
        super(cause);
    }
}
