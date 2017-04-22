package wiki.chenxun.ace.core.base.config;

import java.util.Observer;

/**
 * @Description: Created by chenxun on 2017/4/22.
 */
public interface ConfigBeanAware<T> extends Observer{

    void  setConfigBean(T t);




}
