package wiki.chenxun.ace.core.base.container.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.container.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
public class SpringContainer implements Container {

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
                new AnnotationConfigApplicationContext();
        Config config = ExtendLoader.getExtendLoader(Config.class).getExtension(ExtendLoader.DEFAULT_SPI_NAME);
        ConfigBeanFactoryPostProcessor configBeanFactoryPostProcessor = new ConfigBeanFactoryPostProcessor(config);
        applicationContext.addBeanFactoryPostProcessor(configBeanFactoryPostProcessor);

        applicationContext.scan(scanPackageList.toArray(new String[scanPackageList.size()]));

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
