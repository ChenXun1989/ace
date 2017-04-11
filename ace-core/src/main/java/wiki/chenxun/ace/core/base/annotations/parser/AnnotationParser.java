package wiki.chenxun.ace.core.base.annotations.parser;

import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public interface AnnotationParser {

    /**
     * 解析注解
     * @param classSet set
     */
    void  parser(Set<Class<?>> classSet);



}
