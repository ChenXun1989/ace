package wiki.chenxun.ace.core.base.container;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;
import wiki.chenxun.ace.core.base.config.Config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @Description: Created by chenxun on 2017/4/18.
 */
public class ConfigBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final Config config;

    public ConfigBeanFactoryPostProcessor(Config config) {
        this.config = config;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        Iterator<String> iterator = configurableListableBeanFactory.getBeanNamesIterator();

        while (iterator.hasNext()) {
            Object obj = configurableListableBeanFactory.getBean(iterator.next());

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().isAnnotationPresent(ConfigBean.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, config.configBean(field.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
