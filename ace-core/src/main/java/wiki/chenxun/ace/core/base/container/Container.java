package wiki.chenxun.ace.core.base.container;


import wiki.chenxun.ace.core.base.annotations.Spi;

import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/10.
 */
@Spi("spring")
public interface Container   {

    /**
     * aceServicePackage
     */
    String ACE_SERVICE_PACKAGE = "aceServicePackage:";

    void init( String[] args);

    void start();

    void registerShutdownHook();

    void stop();

    <T> T getBean(Class<T> t);

    <T> T getBean(Class<T> t,String name);











}
