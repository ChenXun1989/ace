package wiki.chenxun.ace.core.base.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.config.ConfigBeanAware;
import wiki.chenxun.ace.core.base.remote.MethodDefine;
import wiki.chenxun.ace.core.base.support.MethodParameter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Observable;


/**
 * @Description: Created by chenxun on 2017/4/22.
 */
public class AceServiceBean<T>  {

    @Getter
    @Setter
    private String  path;

    @Getter
    @Setter
    private volatile T instance;


    public Object exec(String uri, AceHttpMethod aceHttpMethod, Map<String, List<String>> requestMap, String jsonStr) {
        MethodDefine method = Context.getMethodDefine(this, aceHttpMethod);

        Object result = null;
        try {
            if (aceHttpMethod.equals(AceHttpMethod.GET) || aceHttpMethod.equals(AceHttpMethod.DELETE)) {
                result = excuteByParamters(method, requestMap);
            } else if (aceHttpMethod.equals(AceHttpMethod.POST) || aceHttpMethod.equals(AceHttpMethod.PUT)) {

                if (jsonStr != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode node = objectMapper.reader().readTree(jsonStr);
                    result = this.excuteByJson(method, objectMapper, node);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get 或post表单提交执行逻辑
     *
     * @param method
     * @param paramterMap
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */

    private Object excuteByParamters(MethodDefine method, Map<String, List<String>> paramterMap) throws InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[method.getParameters().size()];
        for (MethodParameter methodParameter : method.getParameters()) {
            String parameterName = methodParameter.getParameterName();
            Object paramterValue = parseObject(paramterMap.get(parameterName), methodParameter.getParameterType());

            //System.out.println("request:\t" + parameterName + "=" + paramterValue);

            args[methodParameter.getParameterIndex()] = paramterValue;
        }
        return method.getMethod().invoke(this.instance, args);
    }

    private Object excuteByJson(MethodDefine methodDefine, ObjectMapper objectMapper, JsonNode node) throws IOException, InvocationTargetException, IllegalAccessException {
        Object[] args = new Object[methodDefine.getParameters().size()];
        for (MethodParameter methodParameter : methodDefine.getParameters()) {
            args[methodParameter.getParameterIndex()] = objectMapper.readValue(node.get(methodParameter.getParameterName()).traverse(), methodParameter.getParameterType());
        }
        return methodDefine.getMethod().invoke(this.instance, args);
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
     *
     * @param requestParamter 请求参数聚合list
     * @return value
     */
    private String getParamterStr(List<String> requestParamter) {
        return requestParamter.get(0);
    }


}
