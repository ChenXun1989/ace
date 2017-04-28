package wiki.chenxun.ace.core.base.config;

import wiki.chenxun.ace.core.base.annotations.Spi;


/**
 * @Description: Created by chenxun on 2017/4/16.
 */
public interface Config {

    String ROOT_PATH = "/ace/config/";

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

    void export();

    void clean();


}
