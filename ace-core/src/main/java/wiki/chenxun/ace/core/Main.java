package wiki.chenxun.ace.core;

import wiki.chenxun.ace.core.base.annotations.parser.AnnotationParserRegister;
import wiki.chenxun.ace.core.base.common.Context;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.container.ApplicationProperties;
import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.support.ScanUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 项目启动类
 * Created by chenxun on 2017/4/7.
 */
public final class Main {
    /**
     * 服务运行标示，0运行，其他停止
     */
    private static volatile int flag = 0;


    private Main() {

    }

    /**
     * 入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {

        ApplicationProperties applicationProperties = initApplicationConfig(args);

        ExtendLoader<Container> loader = ExtendLoader.getExtendLoader(Container.class);
        Container container = loader.getExtension(applicationProperties.getContainer());
        Context.setCurrentContainer(container);
        container.init(args);
        container.registerShutdownHook();
        container.start();
        Context.notifyObservers(Context.Event.STARTED);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Context.notifyObservers(Context.Event.STOPED);
                synchronized (Main.class) {
                    flag = 1;
                }
            }
        }));


        synchronized (Main.class) {
            while (flag == 0) {
                try {
                    Main.class.wait();
                } catch (InterruptedException ignore) {

                }
            }
        }

        System.out.println("ace Server stop !!!");


    }

    private static ApplicationProperties initApplicationConfig(String[] args) {

        List<String> scanPackageList = new ArrayList<>();
        scanPackageList.add("wiki.chenxun.ace.core");
        if (null != args && args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith(Container.ACE_SERVICE_PACKAGE)) {
                    scanPackageList.add(arg.substring(arg.indexOf(Container.ACE_SERVICE_PACKAGE) + Container.ACE_SERVICE_PACKAGE.length()));
                }
            }
        }

        Config config = ExtendLoader.getExtendLoader(Config.class).getExtension(ExtendLoader.DEFAULT_SPI_NAME);
        AnnotationParserRegister annotationParserRegister = new AnnotationParserRegister();
        annotationParserRegister.register();

        //扫描
        Set<Class<?>> classSet = ScanUtil.findFileClass(scanPackageList.toArray(new String[scanPackageList.size()]));
        //解析配置。
        Map<Class<? extends Annotation>, Set<Class<?>>> map = new HashMap<>();
        for (Class cls : classSet) {
            for (Class annotationClass : annotationParserRegister.getAnnotations()) {
                if (cls.isAnnotationPresent(annotationClass)) {
                    Set<Class<?>> set = map.get(annotationClass);
                    if (set == null) {
                        set = new HashSet<>();
                        map.put(annotationClass, set);
                    }
                    set.add(cls);
                }
            }


        }
        for (Map.Entry<Class<? extends Annotation>, Set<Class<?>>> entry : map.entrySet()) {
            annotationParserRegister.getParser(entry.getKey()).parser(entry.getValue());
        }

        return config.configBean(ApplicationProperties.class);
    }

}
