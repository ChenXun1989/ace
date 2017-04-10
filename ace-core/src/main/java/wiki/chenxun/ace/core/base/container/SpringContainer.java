package wiki.chenxun.ace.core.base.container;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public class SpringContainer extends Observable implements Container {

    /**
     * 默认的ace 核心包路径
     */
    public static final String ACE_CORE_PACKAGE = "wiki.chenxun.ace.core";
    /**
     * spring 容器上下文
     */
    private AnnotationConfigApplicationContext applicationContext;

    @Override
    public void init(String[] args) {
        List<String> scanPackageList = new ArrayList<>();
        scanPackageList.add(ACE_CORE_PACKAGE);
        if (null != args && args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith(ACE_SERVICE_PACKAGE)) {
                    scanPackageList.add(arg.substring(arg.indexOf(ACE_SERVICE_PACKAGE) + ACE_SERVICE_PACKAGE.length()));
                }
            }
        }
        applicationContext =
                new AnnotationConfigApplicationContext(scanPackageList.toArray(new String[scanPackageList.size()]));
    }

    @Override
    public void start() {
        applicationContext.start();
        notifyObservers();
    }

    @Override
    public void registerShutdownHook() {
        applicationContext.registerShutdownHook();
    }

    @Override
    public void stop() {
        applicationContext.close();
    }

    @Override
    public <T> T getBean(Class<T> t) {
        return applicationContext.getBean(t);
    }

    @Override
    public <T> T getBean(Class<T> t, String name) {
        Object bean = applicationContext.getBean(name);
        if (bean == null) {
            return null;
        }
        if (t.equals(bean.getClass())) {
            return (T) bean;
        }
        return null;
    }


}
