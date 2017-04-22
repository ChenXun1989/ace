package wiki.chenxun.ace.core.base.common;

import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.config.ConfigBeanAware;
import wiki.chenxun.ace.core.base.config.DefaultConfig;
import wiki.chenxun.ace.core.base.container.Container;
import wiki.chenxun.ace.core.base.register.Register;
import wiki.chenxun.ace.core.base.remote.Server;
import wiki.chenxun.ace.core.base.support.ScanUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/21.
 */
public class AceApplication implements ConfigBeanAware<AceApplicationConfig> {

    private String basePage = "wiki.chenxun.ace.core";

    private AceApplicationConfig aceApplicationConfig;

    private volatile int state = 0;

    private Config config;

    private Container container;

    private Server server;

    private Register register;

    @Override
    public void setConfigBean(AceApplicationConfig aceApplicationConfig) {
        this.aceApplicationConfig = aceApplicationConfig;
    }

    public void scan() {

        config = DefaultConfig.INSTANCE;
        if (aceApplicationConfig == null) {
            aceApplicationConfig = (AceApplicationConfig) config.configBeanParser(AceApplicationConfig.class).getConfigBean();
        }

        Set<Class<?>> baseSet = ScanUtil.findFileClass(basePage);
        String[] packages = aceApplicationConfig.getPackages().split(",");
        //扫描
        Set<Class<?>> classSet = ScanUtil.findFileClass(packages);
        classSet.addAll(baseSet);
        for (Class cls : classSet) {
            initAceServiceBean(cls);
        }
        config.configBeanParser(AceApplicationConfig.class).addObserver(this);
        initContainer(packages);
    }

    private void initAceServiceBean(Class cls) {
        if (cls.isAnnotationPresent(AceService.class)) {
            AceServiceBean aceServiceBean = new AceServiceBean();
            try {
                aceServiceBean.setInstance(cls.newInstance());
                AceService aceService = (AceService) cls.getAnnotation(AceService.class);
                aceServiceBean.setPath(aceService.path());
                register(aceServiceBean);

            } catch (InstantiationException e) {
                //TODO： 异常处理
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                //TODO： 异常处理
                e.printStackTrace();
            }

        }

    }

    /**
     * 初始化Method
     *
     * @param clazz  aceService
     * @param method method
     */
    private void initAceServiceMethod(Class<?> clazz, Method method) throws IOException {
        for (AceHttpMethod aceHttpMethod : AceHttpMethod.values()) {
            if (method.isAnnotationPresent(aceHttpMethod.getAnnotationClazz())) {
                Context.putAceServiceMethodMap(clazz, aceHttpMethod, method);
                return;
            }
        }
    }


    private void initContainer(String... packages) {
        container = ExtendLoader.getExtendLoader(Container.class).getExtension(aceApplicationConfig.getContainer());
        container.init(packages);
        container.start();
        for (AceServiceBean aceServiceBean : Context.beans()) {
            Class cls = aceServiceBean.getInstance().getClass();
            Object bean = container.getBean(cls);
            if (bean != null) {
                aceServiceBean.setInstance(bean);
            }
        }
        container.registerShutdownHook();

    }


    @Override
    public void update(Observable o, Object arg) {

    }

    public enum Event {
        START;
    }


    public void register(AceServiceBean... aceServiceBeans) {
        for (AceServiceBean aceServiceBean : aceServiceBeans) {
            Context.putAceServiceMap(aceServiceBean);
            Class cls = aceServiceBean.getInstance().getClass();
            for (Method method : cls.getMethods()) {
                try {
                    initAceServiceMethod(cls, method);
                } catch (IOException e) {
                    //TODO: 异常处理
                    e.printStackTrace();
                }
            }
        }

        register = ExtendLoader.getExtendLoader(Register.class).getExtension(aceApplicationConfig.getRegister());
        //TODO: 注册服务
    }

    public void start() {

        server = ExtendLoader.getExtendLoader(Server.class).getExtension(aceApplicationConfig.getServer());

        server.setConfigBean((AceServerConfig) config.configBeanParser(AceServerConfig.class).getConfigBean());
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (Exception e) {
                    // TODO:异常
                }
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();


    }


    public void close() {
        Runtime.getRuntime().removeShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    server.close();
                    container.stop();
                    state = 1;
                    System.out.println("shutdown !!!");
                    AceApplication.class.notifyAll();
                }
            }
        }));

        while (state == 0) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
        }

        //TODO:结束
    }


}
