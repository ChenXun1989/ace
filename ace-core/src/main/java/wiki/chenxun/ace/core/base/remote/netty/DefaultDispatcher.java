package wiki.chenxun.ace.core.base.remote.netty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
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
import java.math.BigDecimal;
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
        Object[] args;
        String uri = request.uri();
        System.out.println("uri:\t" + uri);
        if (uri.endsWith("favicon.ico")) {
            return "";
        }
        Object aceServiceBean = Context.getBean(uri);
        AceHttpMethod aceHttpMethod = AceHttpMethod.getAceHttpMethod(request.method().toString());

        MethodDefine method = Context.getMethodDefine(uri, aceHttpMethod);

        String contentType = request.headers().get("Content-Type");

        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());

        if (aceHttpMethod.equals(AceHttpMethod.GET) || aceHttpMethod.equals(AceHttpMethod.DELETE)) {
            return this.excuteByParamters(aceServiceBean, method, decoder.parameters());
        } else if (aceHttpMethod.equals(AceHttpMethod.POST) || aceHttpMethod.equals(AceHttpMethod.PUT)) {
            //如果要多次解析，请用 request.content().copy()
            String jsonStr = request.content().toString(CharsetUtil.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode node = objectMapper.reader().readTree(jsonStr);
                return this.excuteByJson(aceServiceBean, method, objectMapper, node);
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


    private Object excuteByJson(Object aceServiceBean, MethodDefine methodDefine, ObjectMapper objectMapper, JsonNode node) throws IOException, InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[methodDefine.getParameters().size()];
        for (MethodParameter methodParameter : methodDefine.getParameters()) {
            args[methodParameter.getParameterIndex()] = objectMapper.readValue(node.get(methodParameter.getParameterName()).traverse(), methodParameter.getParameterType());
        }
        return methodDefine.getMethod().invoke(aceServiceBean, args);
    }

    /**
     * get 或post表单提交执行逻辑
     *
     * @param aceServiceBean
     * @param method
     * @param paramterMap
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object excuteByParamters(Object aceServiceBean, MethodDefine method, Map<String, List<String>> paramterMap) throws InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[method.getParameters().size()];
        for (MethodParameter methodParameter : method.getParameters()) {
            String parameterName = methodParameter.getParameterName();
            Object paramterValue = parseObject(paramterMap.get(parameterName), methodParameter.getParameterType());

            System.out.println("request:\t" + parameterName + "=" + paramterValue);

            args[methodParameter.getParameterIndex()] = paramterValue;
        }
        return method.getMethod().invoke(aceServiceBean, args);
    }


    private Object parseObject(List<String> requestParamter, Class<?> paramterType) {
        if (String.class.isAssignableFrom(paramterType)) {
            return getParamterStr(requestParamter);
        } else if (Integer.class.isAssignableFrom(paramterType) || int.class.isAssignableFrom(paramterType)) {
            return Integer.parseInt(getParamterStr(requestParamter));
        } else if (Long.class.isAssignableFrom(paramterType) || long.class.isAssignableFrom(paramterType)) {
            return Long.parseLong(getParamterStr(requestParamter));
        } else if (Boolean.class.isAssignableFrom(paramterType) || boolean.class.isAssignableFrom(paramterType)) {
            return Boolean.parseBoolean(getParamterStr(requestParamter));
        } else if (Double.class.isAssignableFrom(paramterType) || double.class.isAssignableFrom(paramterType)) {
            return Double.parseDouble(getParamterStr(requestParamter));
        } else if (Float.class.isAssignableFrom(paramterType) || float.class.isAssignableFrom(paramterType)) {
            return Float.parseFloat(getParamterStr(requestParamter));
        } else if (Short.class.isAssignableFrom(paramterType) || short.class.isAssignableFrom(paramterType)) {
            return Short.parseShort(getParamterStr(requestParamter));
        }
        return getParamterStr(requestParamter);
    }

    /**
     * 获取请求参数 value
     * @param requestParamter 请求参数聚合list
     * @return value
     */
    private String getParamterStr(List<String> requestParamter) {
        return requestParamter.get(0);
    }

}
