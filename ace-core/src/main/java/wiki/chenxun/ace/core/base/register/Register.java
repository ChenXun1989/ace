package wiki.chenxun.ace.core.base.register;

import wiki.chenxun.ace.core.base.annotations.Spi;
import wiki.chenxun.ace.core.base.common.ShutDownClearer;

/**
 * @Description: Created by chenxun on 2017/4/12.
 */
@Spi("zookeeper")
public interface Register extends ShutDownClearer {

    String ROOT="/ace";

    void register();

    void unregister();





}
