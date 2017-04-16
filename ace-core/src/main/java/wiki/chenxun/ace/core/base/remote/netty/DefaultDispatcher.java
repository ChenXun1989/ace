package wiki.chenxun.ace.core.base.remote.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.common.ApplicationInfo;
import wiki.chenxun.ace.core.base.common.Context;
import wiki.chenxun.ace.core.base.remote.Dispatcher;
import wiki.chenxun.ace.core.base.remote.MethodDefine;
import wiki.chenxun.ace.core.base.support.MethodParameter;

import javax.script.ScriptEngine;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * @Description: 核心类，单例
 * Created by chenxun on 2017/4/8.
 */
public class DefaultDispatcher implements Dispatcher {

    private Object parseObject(List<String> requestParamter, Class<?> paramterType) {
        if (String.class.isAssignableFrom(paramterType)) {
            return requestParamter.get(0);
        } else if (Integer.class.isAssignableFrom(paramterType) || int.class.isAssignableFrom(paramterType)) {
            return Integer.parseInt(requestParamter.get(0));
        } else {
            return null;
        }
    }

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
        Object[] args;
        String uri = request.uri();
        System.out.println("uri:\t" + uri);
        if (uri.endsWith("favicon.ico")) {
            return "";
        }
        Object aceServiceBean = Context.getBean(uri);
        MethodDefine method = null;
        if (request.method().equals(HttpMethod.GET)) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            method = Context.getMethodDefine(uri, AceHttpMethod.GET);
            args = new Object[method.getParameters().size()];
            Map<String, List<String>> map = decoder.parameters();
            for (MethodParameter methodParameter : method.getParameters()) {
                String parameterName = methodParameter.getParameterName();
                Object paramterValue = parseObject(map.get(parameterName), methodParameter.getParameterType());
                System.out.println("request:\t" + parameterName + "=" + paramterValue);
                args[methodParameter.getParameterIndex()] = paramterValue;
            }

            System.out.println(decoder.parameters());
            return method.getMethod().invoke(aceServiceBean, args);
        } else if (request.method().equals(HttpMethod.POST)) {
            //如果要多次解析，请用 request.content().copy()
            String jsonStr = request.content().toString(CharsetUtil.UTF_8);
            ObjectMapper om = new ObjectMapper();
            try {
                JsonNode node = om.reader().readTree(jsonStr);

                System.out.println(node);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }







        // TODO:路由方法


        // TODO:调用方法
        System.out.println(request.uri());
        ApplicationInfo mock = new ApplicationInfo();
        mock.setName("ace");
        mock.setVersion("1.0");
        mock.setDesc(" mock  !!! ");
        return mock;
    }


}
