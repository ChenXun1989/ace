package wiki.chenxun.ace.core.base.remote.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.common.AceServiceBean;
import wiki.chenxun.ace.core.base.common.ApplicationInfo;
import wiki.chenxun.ace.core.base.common.Context;
import wiki.chenxun.ace.core.base.remote.Dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    public Object doDispatcher(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        Object[] args;
        String uri = request.uri();
        if (uri.endsWith("favicon.ico")) {
            return "";
        }

        AceServiceBean aceServiceBean = Context.getAceServiceBean(uri);
        AceHttpMethod aceHttpMethod = AceHttpMethod.getAceHttpMethod(request.method().toString());
        ByteBuf content = request.content();
        //如果要多次解析，请用 request.content().copy()
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> requestMap = decoder.parameters();
        Object result = aceServiceBean.exec(uri, aceHttpMethod, requestMap, content == null ? null : content.toString(CharsetUtil.UTF_8));
        String contentType = request.headers().get("Content-Type");
        if (result == null) {
            ApplicationInfo mock = new ApplicationInfo();
            mock.setName("ace");
            mock.setVersion("1.0");
            mock.setDesc(" mock  !!! ");
            result = mock;
        }
        return result;

    }


}
