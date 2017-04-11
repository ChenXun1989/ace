package wiki.chenxun.ace.core.base.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by robin.tian on 17-4-7.
 */
public   class MethodParameter  {
    /**
     * 方法
     */
    private  Method method;
    /**
     * 参数下标
     */
    private  int parameterIndex;
    /**
     * 参数类型
     */
    private volatile Class<?> parameterType;
    /**
     * 参数类型及泛型
     */
    private volatile Type genericParameterType;
    /**
     * 参数名
     */
    private volatile String parameterName;
    /**
     * 参数Annotation
     */
    private volatile Annotation[] parameterAnnotations;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }


    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Type getGenericParameterType() {
        return genericParameterType;
    }

    public void setGenericParameterType(Type genericParameterType) {
        this.genericParameterType = genericParameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(Annotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }
}
