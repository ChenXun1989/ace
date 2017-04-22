package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.remote.MethodDefine;
import wiki.chenxun.ace.core.base.support.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public final class Context {

    /**
     * class -> AceHttpMethod -> Method
     */
    private static final Map<Class<?>, Map<AceHttpMethod, MethodDefine>> ACESERVICE_METHOD_MAP =
            new ConcurrentHashMap<Class<?>, Map<AceHttpMethod, MethodDefine>>();

    /**
     * 是否初始化完成
     * 防止解析冲突
     */
    private static boolean initAnalyzed = false;
    /**
     *
     */
    private static final Map<String, AceServiceBean> ACE_SERVICE_BEAN_MAP = new ConcurrentHashMap<>();


    public static AceServiceBean getAceServiceBean(String uri) {
        String aceUri = uri.split("\\?")[0];
        return ACE_SERVICE_BEAN_MAP.get(aceUri);
    }

    public static Collection<AceServiceBean> beans() {
        return ACE_SERVICE_BEAN_MAP.values();
    }

    ;


    /**
     * 标识解析元数据完成
     */
    public static void initAnalyzed() {
        initAnalyzed = true;
    }


    /**
     * 初始化 aceServiceHttpMethodMap
     *
     * @param clazz         class
     * @param aceHttpMethod method enum
     * @param method        method
     */
    public static void putAceServiceMethodMap(Class<?> clazz, AceHttpMethod aceHttpMethod, Method method) throws IOException {
        if (!initAnalyzed) {
            if (ACESERVICE_METHOD_MAP.containsKey(clazz)) {
                if (ACESERVICE_METHOD_MAP.get(clazz).containsKey(aceHttpMethod)) {
                    throw new RuntimeException("重复HttpMethod定义");
                } else {
                    ACESERVICE_METHOD_MAP.get(clazz).put(aceHttpMethod, Context.createMethodDefine(clazz, aceHttpMethod, method));
                }
            } else {
                Map<AceHttpMethod, MethodDefine> map = new ConcurrentHashMap<AceHttpMethod, MethodDefine>();
                map.put(aceHttpMethod, Context.createMethodDefine(clazz, aceHttpMethod, method));
                ACESERVICE_METHOD_MAP.put(clazz, map);
            }
        }
    }


    /**
     * @param aceService
     * @param aceServiceBean
     */
    public static void putAceServiceMap(AceServiceBean aceServiceBean) {
        if (!initAnalyzed) {
            String uri = aceServiceBean.getPath();
            if (uri != null && !uri.startsWith("/")) {
                throw new RuntimeException("自定义path 须  / 开头");
            }
            String aceUri = Context.getClassUri(aceServiceBean.getInstance().getClass());
            if (ACE_SERVICE_BEAN_MAP.containsKey(uri) || ACE_SERVICE_BEAN_MAP.containsKey(aceUri)) {
                throw new RuntimeException("aceService Path 重复定义异常");
            }
            //TODO ace uri 定义
            ACE_SERVICE_BEAN_MAP.put(uri, aceServiceBean);
            ACE_SERVICE_BEAN_MAP.put(aceUri, aceServiceBean);
        }
    }


    /**
     * 获取method define
     *
     * @param uri           uri
     * @param aceHttpMethod ace http method
     * @return method define
     */
    public static MethodDefine getMethodDefine(AceServiceBean aceServiceBean, AceHttpMethod aceHttpMethod) {
        Class cls = aceServiceBean.getInstance().getClass();
        return ACESERVICE_METHOD_MAP.get(cls).get(aceHttpMethod);
    }


    /**
     * 获取内部使用的class uri
     *
     * @param clazz
     * @return
     */
    public static String getClassUri(Class<?> clazz) {
        return clazz.getName();
    }


    /**
     * 获取Method uri
     *
     * @param clazz  clazz
     * @param method method
     * @return method uri
     */
    public static String getMethodUri(Class<?> clazz, Method method) {
        return Context.getClassUri(clazz) + "." + method.getName();
    }


    /**
     * 解析Method元数据
     *
     * @param method method
     * @return aceMethod定义
     */
    public static MethodDefine createMethodDefine(Class<?> clazz, AceHttpMethod aceHttpMethod, Method method) throws IOException {
        MethodDefine methodDefine = new MethodDefine();
        //TODO method元数据收集 frank
        methodDefine.setAceHttpMethod(aceHttpMethod);
        methodDefine.setPath(Context.getMethodUri(clazz, method));
        methodDefine.setMethod(method);
        methodDefine.setParameters(ReflectUtil.getMethodParamNames(method));
        return methodDefine;
    }


}
