package wiki.chenxun.ace.core.base.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@Component
public final class SpringBeanUtil implements ApplicationContextAware {
    /**
     * spring容器上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 关闭spring容器
     */
    public static void closeContext() {
        if (applicationContext instanceof AnnotationConfigApplicationContext) {
            AnnotationConfigApplicationContext annotationConfigApplicationContext = (AnnotationConfigApplicationContext) applicationContext;
            annotationConfigApplicationContext.close();
        }
    }

    /**
     * 获取spripng容器里面对象
     * @param cls Class
     * @param <T> 范型
     * @return bean实例对象
     */
    public static <T> T getBean(Class<T> cls) {
        return SpringBeanUtil.applicationContext.getBean(cls);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }
}
