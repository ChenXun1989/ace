package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.remote.MethodDefine;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public final class Context {

    /**
     * aceServiceMap实例集合
     */
    private final Map<String, Object> aceServiceMap = new HashMap<>();


    /**
     * uri-path 与 method的映射
     */
    private final Map<String, MethodDefine> pathMap = new HashMap<>();


    /**
     * Observable
     */
    private static ContainerObservable containerObservable = new ContainerObservable();

    /**
     * Observable
     */
    private static class ContainerObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
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
     * 当前容器
     */
    private static Container currentContainer;

    /**
     * getCurrentContainer
     * @return Container
     */
    public static Container getCurrentContainer() {
        return currentContainer;
    }

    /**
     * setCurrentContainer
     * @param currentContainer 当前ioc容器
     */
    public static void setCurrentContainer(Container currentContainer) {

        Context.currentContainer = currentContainer;
        containerObservable.addObserver(ExtendLoader.getExtendLoader(Container.class));

    }

    /**
     * 通知观察者
     * @param event 事件类型
     */
    public static void notifyObservers(Event event) {
        containerObservable.setChanged();
        containerObservable.notifyObservers(event);
    }

    /**
     * ioc容器里面获取对象
     * @param t ioc容器里面的bean
     * @param <T> ioc容器里面的bean
     * @return ioc容器里面的bean
     */
    public static <T> T getBean(Class<T> t) {
        return currentContainer.getBean(t);
    }


}
