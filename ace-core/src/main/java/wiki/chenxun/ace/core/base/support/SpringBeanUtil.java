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
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void closeContext(){
        if(applicationContext instanceof AnnotationConfigApplicationContext ){
            AnnotationConfigApplicationContext annotationConfigApplicationContext=(AnnotationConfigApplicationContext)applicationContext;
            annotationConfigApplicationContext.close();
        }
    }


    public static  <T> T getBean(Class<T> cls){
       return SpringBeanUtil.applicationContext.getBean(cls);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext=applicationContext;
    }
}
