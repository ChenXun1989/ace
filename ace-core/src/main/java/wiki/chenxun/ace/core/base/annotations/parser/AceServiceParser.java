package wiki.chenxun.ace.core.base.annotations.parser;

import wiki.chenxun.ace.core.base.annotations.AceHttpMethod;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.common.Context;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/11.
 */
public class AceServiceParser implements AnnotationParser {

    @Override
    public void parser(Set<Class<?>> classSet) {
        Iterator<Class<?>> iterator = classSet.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            AceService aceService = clazz.getAnnotation(AceService.class);
            Context.putAceServiceMap(aceService, clazz);
            for (Method method : clazz.getMethods()) {
                this.initAceServiceMethod(clazz, method);
            }

        }
        Context.initAnalyzed();   //TODO 解析完成标记
    }

    /**
     * 初始化Method
     *
     * @param clazz  aceService
     * @param method method
     */
    private void initAceServiceMethod(Class<?> clazz, Method method) {
        for (AceHttpMethod aceHttpMethod : AceHttpMethod.values()) {
            if (method.isAnnotationPresent(aceHttpMethod.getAnnotationClazz())) {
                Context.putAceServiceMethodMap(clazz, aceHttpMethod, Context.createMethodDefine(method));
                return;
            }
        }
    }

}
