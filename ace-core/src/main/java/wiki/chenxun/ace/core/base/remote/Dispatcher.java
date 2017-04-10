package wiki.chenxun.ace.core.base.remote;

import io.netty.handler.codec.http.FullHttpRequest;
import wiki.chenxun.ace.core.base.annotations.Spi;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */

@Spi("netty")
public interface Dispatcher {
    /**
     * 请求分发与处理
     * @param request 请求对象
     * @return 处理结果
     * @throws Exception 异常基类
     */
    Object doDispatcher(FullHttpRequest request) throws Exception;


}
