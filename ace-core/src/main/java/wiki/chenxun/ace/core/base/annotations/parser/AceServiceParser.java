package wiki.chenxun.ace.core.base.annotations.parser;

import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public class AceServiceParser implements AnnotationParser {


    @Override
    public void parser(Set<Class<?>> classSet) {
        System.out.println(classSet);
    }
}
