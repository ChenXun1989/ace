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



    private static ContainerObservable containerObservable = new ContainerObservable();

    private static class ContainerObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }


    public static enum Event {
        STARTED,STOPED;
    }

    private static Container currentContainer;

    public static Container getCurrentContainer() {
        return currentContainer;
    }


    public static void setCurrentContainer(Container currentContainer) {

        Context.currentContainer = currentContainer;
        containerObservable.addObserver(ExtendLoader.getExtendLoader(Container.class));

    }

    public static void notifyObservers(Event event) {
        containerObservable.setChanged();
        containerObservable.notifyObservers(event);
    }


    public static <T> T getBean(Class<T> t) {
        return currentContainer.getBean(t);
    }





}
