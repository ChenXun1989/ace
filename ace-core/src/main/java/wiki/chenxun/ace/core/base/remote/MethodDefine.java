package wiki.chenxun.ace.core.base.remote;

import lombok.Data;
import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.support.MethodParameter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/9.
 */
@Data
public class MethodDefine implements Serializable {

    /**
     * HTTP method
     */
    private AceHttpMethod aceHttpMethod;

    /**
     * 方法内参
     */
    private List<MethodParameter> parameters = new ArrayList<MethodParameter>();

    /**
     * 请求uri
     */
    private String path;
    /**
     * 处理方法
     */
    private Method method;

}
