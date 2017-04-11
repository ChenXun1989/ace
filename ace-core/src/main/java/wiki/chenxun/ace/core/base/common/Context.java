package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.remote.MethodDefine;

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
     * @param methodDefine  method
     */
    public static void putAceServiceMethodMap(Class<?> clazz, AceHttpMethod aceHttpMethod, MethodDefine methodDefine) {
        if (!initAnalyzed) {
            if (ACESERVICE_METHOD_MAP.containsKey(clazz)) {
                if (ACESERVICE_METHOD_MAP.get(clazz).containsKey(aceHttpMethod)) {
                    throw new RuntimeException("重复HttpMethod定义");
                } else {
                    ACESERVICE_METHOD_MAP.get(clazz).put(aceHttpMethod, methodDefine);
                }
            } else {
                Map<AceHttpMethod, MethodDefine> map = new ConcurrentHashMap<AceHttpMethod, MethodDefine>();
                map.put(aceHttpMethod, methodDefine);
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
            String aceUri = clazz.getName();
            if (ACESERVICE_MAP.containsKey(uri) || ACESERVICE_MAP.containsKey(aceUri)) {
                throw new RuntimeException("aceService Path 重复定义异常");
            }
            System.out.println(uri + "->" + aceUri);
            //TODO ace uri 定义
            ACESERVICE_MAP.put(uri, clazz);
            ACESERVICE_MAP.put(aceUri, clazz);
        }
    }

    /**
     * 解析Method元数据
     *
     * @param method method
     * @return aceMethod定义
     */
    public static MethodDefine createMethodDefine(Method method) {
        MethodDefine methodDefine = new MethodDefine();
        //TODO method元数据收集 frank

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
