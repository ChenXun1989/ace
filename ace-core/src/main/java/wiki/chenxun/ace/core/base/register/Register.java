package wiki.chenxun.ace.core.base.register;

import wiki.chenxun.ace.core.base.annotations.Spi;
import wiki.chenxun.ace.core.base.config.ConfigBeanAware;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
@Spi("zookeeper")
public interface Register extends ConfigBeanAware<RegisterConfig> {

    String ROOT="/ace";

    void register();

    void unregister();





}
