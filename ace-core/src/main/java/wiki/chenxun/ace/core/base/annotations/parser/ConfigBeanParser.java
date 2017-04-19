package wiki.chenxun.ace.core.base.annotations.parser;


import wiki.chenxun.ace.core.base.annotations.ConfigBean;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.Config;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;
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
        Object obj = null;
        BeanInfo beanInfo = null;
        try {
            obj = cls.newInstance();
            beanInfo = Introspector.getBeanInfo(cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //MissingResourceException

        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String key = prefix + "." + propertyDescriptor.getName();
            Method method = propertyDescriptor.getWriteMethod();
            if (method != null) {
                try {
                    String value = resource.getString(key);
                    Class paramClass = method.getParameterTypes()[0];
                    method.invoke(obj, parse(paramClass, value));
                } catch (MissingResourceException e) {
                    //TODO： 记录日志
                } catch (IllegalAccessException e) {
                    //TODO： 记录日志
                } catch (InvocationTargetException e) {
                    //TODO： 记录日志
                }
            }

        }
        return obj;
    }

    private Object parse(Class cls, String value) {
        if (int.class.equals(cls)) {
            return Integer.parseInt(value);
        } else if (long.class.equals(cls)) {
            return Long.parseLong(value);
        } else if (double.class.equals(cls)) {
            return Double.parseDouble(value);
        } else if (float.class.equals(cls)) {
            return Float.parseFloat(value);
        } else if (byte.class.equals(cls)) {
            return Byte.parseByte(value);
        }
        return value;

    }

}
