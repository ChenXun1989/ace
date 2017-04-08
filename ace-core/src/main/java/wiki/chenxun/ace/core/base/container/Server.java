package wiki.chenxun.ace.core.base.container;

import java.io.Closeable;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */
public interface Server extends Closeable{

    void start() throws Exception;

}
