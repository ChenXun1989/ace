package wiki.chenxun.ace.core.base.container.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.config.DefaultConfig;
import wiki.chenxun.ace.core.base.container.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public class SpringContainer implements Container {

    /**
     * spring 容器上下文
     */
    private AnnotationConfigApplicationContext applicationContext;

    @Override
    public void init(String... packages) {
        applicationContext = new AnnotationConfigApplicationContext();
        Config config= DefaultConfig.INSTANCE;
        ConfigBeanFactoryPostProcessor configBeanFactoryPostProcessor = new ConfigBeanFactoryPostProcessor(config);
        applicationContext.addBeanFactoryPostProcessor(configBeanFactoryPostProcessor);
        applicationContext.scan(packages);

    }

    @Override
    public void start() {
        applicationContext.refresh();
        applicationContext.start();
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
