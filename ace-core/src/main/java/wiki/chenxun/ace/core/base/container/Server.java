package wiki.chenxun.ace.core.base.container;

import java.io.Closeable;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */
public interface Server extends Closeable {
    /**
     *  开启服务
     * @throws Exception 异常基类
     */
    void start() throws Exception;

}
