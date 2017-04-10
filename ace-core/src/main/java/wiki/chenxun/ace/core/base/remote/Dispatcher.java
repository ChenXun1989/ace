package wiki.chenxun.ace.core.base.remote;

import io.netty.handler.codec.http.FullHttpRequest;
import wiki.chenxun.ace.core.base.annotations.Spi;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */

@Spi("netty")
public interface Dispatcher {

     Object doDispatcher(FullHttpRequest request) throws Exception;


}
