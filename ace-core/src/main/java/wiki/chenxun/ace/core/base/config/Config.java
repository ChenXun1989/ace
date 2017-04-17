package wiki.chenxun.ace.core.base.config;

import wiki.chenxun.ace.core.base.annotations.Spi;

import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
@Spi("default")
public interface Config {

    public static final String DEFAULT_PATH = "application";

    /**
     * 获取配置
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> T configBean(Class<T> cls);

    /**
     * @param t
     * @param <T>
     */
    <T> void add(T t);

    /**
     * @param t
     */
    <T>void update(T t);


}
