package wiki.chenxun.ace.core.base.remote;

import wiki.chenxun.ace.core.base.annotations.Spi;

import java.io.Closeable;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */
@Spi("netty")
public interface Server extends Closeable {


    /**
     *  开启服务
     * @throws Exception 异常基类
     */
    void start() throws Exception;



}
