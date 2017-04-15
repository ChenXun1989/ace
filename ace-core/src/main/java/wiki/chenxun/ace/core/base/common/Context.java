package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.remote.MethodDefine;
import wiki.chenxun.ace.core.base.support.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public final class Context {
    /**
     * path - Class
     */
    private static final Map<String, Class<?>> ACESERVICE_MAP = new ConcurrentHashMap<String, Class<?>>();
    /**
     * class -> AceHttpMethod -> Method
     */
    private static final Map<Class<?>, Map<AceHttpMethod, MethodDefine>> ACESERVICE_METHOD_MAP =
            new ConcurrentHashMap<Class<?>, Map<AceHttpMethod, MethodDefine>>();

    /**
     * className - > AceService Bean
     */
    private static final Map<String, String> ACESERVICE_BEAN_NAME_MAP = new ConcurrentHashMap<String, String>();

    /**
     * Observable
     */
    private static ContainerObservable containerObservable = new ContainerObservable();
    /**
     * 是否初始化完成
     * 防止解析冲突
     */
    private static boolean initAnalyzed = false;
    /**
     * 当前容器
     */
    private static Container currentContainer;

    private Context() {

    }

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
     * 初始化aceServiceMap
     *
     * @param aceService AceService
     * @param clazz      class
     */
    public static void putAceServiceMap(AceService aceService, Class<?> clazz) {
        if (!initAnalyzed) {
            String uri = aceService.path();
            if (!uri.startsWith("/")) {
                throw new RuntimeException("自定义path 须  / 开头");
            }
            String aceUri = Context.getClassUri(clazz);
            if (ACESERVICE_MAP.containsKey(uri) || ACESERVICE_MAP.containsKey(aceUri)) {
                throw new RuntimeException("aceService Path 重复定义异常");
            }
            //TODO ace uri 定义
            System.out.println("ACE Service 加载：" + uri + "\t->\t" + clazz.getName());
            ACESERVICE_MAP.put(uri, clazz);
            ACESERVICE_MAP.put(aceUri, clazz);
            ACESERVICE_BEAN_NAME_MAP.put(aceUri, aceService.value().equals("") ? clazz.getCanonicalName() : aceService.value());
        }
    }


    /**
     * 获取method define
     * @param uri uri
     * @param aceHttpMethod ace http method
     * @return method define
     */
    public static MethodDefine getMethodDefine(String uri, AceHttpMethod aceHttpMethod) {
        return ACESERVICE_METHOD_MAP.get(getAceClass(uri)).get(aceHttpMethod);
    }

    private static Class<?> getAceClass(String uri) {
        return ACESERVICE_MAP.get(getAceUri(uri));
    }

    /**
     * 获取系统默认aceUri
     * @param uri uri
     * @return uri
     */
    private static String getAceUri(String uri) {
        String aceUri = uri.split("\\?")[0];
        if (!ACESERVICE_MAP.containsKey(aceUri)){
            aceUri = aceUri.substring(1).replace("/", ".");
            if (ACESERVICE_MAP.containsKey(aceUri)) {
                aceUri = Context.getClassUri(ACESERVICE_MAP.get(aceUri));
            } else {
                throw new RuntimeException("未找到实现注入：" + aceUri);
            }
        }
        return aceUri;
    }

    /**
     * uri获取实例化对象
     *
     * @param uri
     * @return
     */
    public static Object getBean(String uri) {
        return Context.getBean(getAceClass(uri));
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

    /**
     * getCurrentContainer
     *
     * @return Container
     */
    public static Container getCurrentContainer() {
        return currentContainer;
    }

    /**
     * setCurrentContainer
     *
     * @param currentContainer 当前ioc容器
     */
    public static void setCurrentContainer(Container currentContainer) {

        Context.currentContainer = currentContainer;
        containerObservable.addObserver(ExtendLoader.getExtendLoader(Container.class));

    }

    /**
     * 通知观察者
     *
     * @param event 事件类型
     */
    public static void notifyObservers(Event event) {
        containerObservable.setChanged();
        containerObservable.notifyObservers(event);
    }

    /**
     * ioc容器里面获取对象
     *
     * @param t   ioc容器里面的bean
     * @param <T> ioc容器里面的bean
     * @return ioc容器里面的bean
     */
    public static <T> T getBean(Class<T> t) {
        return currentContainer.getBean(t);
    }

    /**
     * 事件类型
     */
    public enum Event {
        /**
         * 已开始
         */
        STARTED,
        /**
         * 已结束
         */
        STOPED;
    }

    /**
     * Observable
     */
    private static class ContainerObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }
}
