package wiki.chenxun.ace.core.base.annotations.parser;

import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public class AnnotationParserRegister {

    /**
     * 注解解析器
     */
    private final Map<Class<? extends Annotation>, AnnotationParser> parserMap = new HashMap<>();

    /**
     * 注册注解解析器
     */
    public void register() {
        // FIXME: 后期改成spi
        parserMap.put(AceService.class, new AceServiceParser());
        parserMap.put(ConfigBean.class, new ConfigBeanParser());
    }

    /**
     * 获取注解对应的解析器
     *
     * @param annotationClass 注解
     * @return 解析器
     */
    public AnnotationParser getParser(Class<? extends Annotation> annotationClass) {
        return parserMap.get(annotationClass);
    }

    /**
     * 获取注解列表
     *
     * @return set
     */
    public Set<Class<? extends Annotation>> getAnnotations() {
        return parserMap.keySet();
    }


}
