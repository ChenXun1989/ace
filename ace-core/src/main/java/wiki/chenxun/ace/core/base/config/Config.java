package wiki.chenxun.ace.core.base.config;

import wiki.chenxun.ace.core.base.annotations.Spi;


/**
 * @Description: Created by chenxun on 2017/4/16.
 */
public interface Config {

    /**
     * DEFAULT_PATH
     */
    String DEFAULT_PATH = "application";

    /**
     *
     * @param cls
     * @return
     */
    ConfigBeanParser configBeanParser(Class cls);

    /**
     * @param parser
     */
    void add(ConfigBeanParser parser);


}
