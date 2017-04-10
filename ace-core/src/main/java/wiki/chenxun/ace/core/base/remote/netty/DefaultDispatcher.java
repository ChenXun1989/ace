package wiki.chenxun.ace.core.base.remote.netty;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import wiki.chenxun.ace.core.base.common.ApplicationInfo;
import wiki.chenxun.ace.core.base.remote.Dispatcher;
import wiki.chenxun.ace.core.base.remote.MethodDefine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 核心类，单例
 * Created by chenxun on 2017/4/8.
 */
public class DefaultDispatcher implements Dispatcher {


    /**
     * 请求分发与处理
     *
     * @param request http协议请求
     * @return 处理结果
     * @throws InvocationTargetException 调用异常
     * @throws IllegalAccessException    参数异常
     */
    @SuppressWarnings("unchecked")
    public Object doDispatcher(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        // TODO:路由方法

        // TODO:调用方法

        ApplicationInfo mock = new ApplicationInfo();
        mock.setName("ace");
        mock.setVersion("1.0");
        mock.setDesc(" mock  !!! ");
        return mock;
    }


}
