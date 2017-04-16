package wiki.chenxun.ace.core.base.annotations.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.expression.spel.ast.Projection;
import wiki.chenxun.ace.core.base.annotations.ConfigBean;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.register.Register;
import wiki.chenxun.ace.core.base.register.zookeeper.ZookeeperRegister;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @Description: Created by chenxun on 2017/4/16.
 */
public class ConfigBeanParser implements AnnotationParser {

    private Config config;


    public ConfigBeanParser() {
        init();
    }

    private void init() {
        config = ExtendLoader.getExtendLoader(Config.class).getExtension(ExtendLoader.DEFAULT_SPI_NAME);

    }

    private ResourceBundle buildResource() {
        return ResourceBundle.getBundle(Config.DEFAULT_PATH);
    }

    @Override
    public void parser(Set<Class<?>> classSet) {
        ResourceBundle resource = buildResource();
        for (Class cls : classSet) {
            Object obj = parserConfigBean(cls, resource);
            config.add(obj);
        }
    }

    public Object parserConfigBean(Class cls, ResourceBundle resource) {
        ConfigBean configBean = (ConfigBean) cls.getAnnotation(ConfigBean.class);
        String prefix = configBean.value();
        try {
            Object obj = cls.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(cls);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : pds) {
                String key = prefix + "." + propertyDescriptor.getName();
                propertyDescriptor.getWriteMethod().invoke(obj, resource.getObject(key));
            }
            return obj;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
