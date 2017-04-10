package wiki.chenxun.ace.core.base.exception;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public class ExtendLoadException extends RuntimeException {


    public ExtendLoadException(String msg) {
        super(msg);
    }

    public ExtendLoadException(String msg, Throwable throwable) {
        super(msg, throwable);
    }


}
