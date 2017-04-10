package wiki.chenxun.ace.core.base.container;


import wiki.chenxun.ace.core.base.annotations.Spi;


/**
 * @Description: Created by chenxun on 2017/4/10.
 */
@Spi("spring")
public interface Container {

    /**
     * aceServicePackage
     */
    String ACE_SERVICE_PACKAGE = "aceServicePackage:";

    /**
     * 初始化容器
     *
     * @param args 启动参数透传
     */
    void init(String[] args);

    /**
     * 容器开始
     */
    void start();

    /**
     * 注册清理钩子
     */
    void registerShutdownHook();

    /**
     * 停止容器
     */
    void stop();

    /**
     * 获取实例
     *
     * @param t   t
     * @param <T> 范型
     * @return 实例
     */
    <T> T getBean(Class<T> t);

    /**
     * 获取实例
     *
     * @param t    class
     * @param name name
     * @param <T>  范型
     * @return 实例
     */
    <T> T getBean(Class<T> t, String name);


}
