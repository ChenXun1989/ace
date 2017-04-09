package wiki.chenxun.ace.core.base.container;

import lombok.Data;

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
     * post请求前缀
     */
    public static final String POST = "_post";

    /**
     * get请求前缀
     */
    public static final String GET = "_get";

    /**
     *  key
     */
    private List<String> paramKeyList = new ArrayList<>();

    /**
     *  class
     */
    private List<Class> paramClassList = new ArrayList<>();
    /**
     * 请求uri
     */
    private String path;
    /**
     * 处理方法
     */
    private Method method;

}
